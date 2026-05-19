package com.vtlamdev.sourcebase;

import com.vtlamdev.sourcebase.cache.SpringTbTransactionalCache;
import com.vtlamdev.sourcebase.cache.TbCacheTransaction;
import com.vtlamdev.sourcebase.cache.TbTransactionalCache;
import org.junit.jupiter.api.Test;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class SpringTbTransactionalCacheTest {

    @Test
    void getOrFetchFromDbCachesValueAfterFirstMiss() {
        TbTransactionalCache<String, String> cache = new SpringTbTransactionalCache<>(new ConcurrentMapCacheManager("user-lookup"), "user-lookup");
        AtomicInteger dbCalls = new AtomicInteger();

        String first = cache.getOrFetchFromDB("alice", () -> {
            dbCalls.incrementAndGet();
            return "alice@example.com";
        }, false, true);
        String second = cache.getOrFetchFromDB("alice", () -> {
            dbCalls.incrementAndGet();
            return "ignored@example.com";
        }, false, true);

        assertThat(first).isEqualTo("alice@example.com");
        assertThat(second).isEqualTo("alice@example.com");
        assertThat(dbCalls).hasValue(1);
    }

    @Test
    void getOrFetchFromDbCanCacheNullValues() {
        TbTransactionalCache<String, String> cache = new SpringTbTransactionalCache<>(new ConcurrentMapCacheManager("tenant-lookup"), "tenant-lookup");
        AtomicInteger dbCalls = new AtomicInteger();

        String first = cache.getOrFetchFromDB("missing", () -> {
            dbCalls.incrementAndGet();
            return null;
        }, true, true);
        String second = cache.getOrFetchFromDB("missing", () -> {
            dbCalls.incrementAndGet();
            return "should-not-be-called";
        }, true, true);

        assertThat(first).isNull();
        assertThat(second).isNull();
        assertThat(dbCalls).hasValue(1);
        assertThat(cache.get("missing")).isNotNull();
        assertThat(cache.get("missing").get()).isNull();
    }

    @Test
    void transactionCommitPopulatesCacheWhenNoConcurrentMutationOccurs() {
        TbTransactionalCache<String, String> cache = new SpringTbTransactionalCache<>(new ConcurrentMapCacheManager("admin-settings"), "admin-settings");
        TbCacheTransaction<String, String> transaction = cache.newTransactionForKey("settings");

        transaction.put("settings", "payload");

        assertThat(transaction.commit()).isTrue();
        assertThat(cache.get("settings")).isNotNull();
        assertThat(cache.get("settings").get()).isEqualTo("payload");
    }

    @Test
    void transactionCommitFailsAfterConcurrentMutationOnTheSameKey() {
        TbTransactionalCache<String, String> cache = new SpringTbTransactionalCache<>(new ConcurrentMapCacheManager("security-settings"), "security-settings");
        TbCacheTransaction<String, String> transaction = cache.newTransactionForKey("jwt");
        transaction.put("jwt", "stale");

        cache.put("jwt", "fresh");

        assertThat(transaction.commit()).isFalse();
        assertThat(cache.get("jwt")).isNotNull();
        assertThat(cache.get("jwt").get()).isEqualTo("fresh");
    }

}
