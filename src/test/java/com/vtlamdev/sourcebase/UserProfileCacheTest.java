package com.vtlamdev.sourcebase;

import com.vtlamdev.sourcebase.cache.CacheNames;
import com.vtlamdev.sourcebase.cache.TbTransactionalCacheFactory;
import com.vtlamdev.sourcebase.cache.UserProfileCache;
import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import org.junit.jupiter.api.Test;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileCacheTest {

    @Test
    void userProfileCacheReturnsCachedUserUntilEvicted() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager(CacheNames.USER_LOOKUP);
        UserProfileCache cache = new UserProfileCache(new TbTransactionalCacheFactory(cacheManager));
        UserId userId = new UserId(UUID.randomUUID());
        AtomicInteger dbCalls = new AtomicInteger();

        User first = cache.getOrFetch(userId, () -> {
            dbCalls.incrementAndGet();
            User user = new User(userId);
            user.setEmail("cached.user@sourcebase.local");
            return user;
        });
        User second = cache.getOrFetch(userId, () -> {
            dbCalls.incrementAndGet();
            User user = new User(userId);
            user.setEmail("should.not.load@sourcebase.local");
            return user;
        });

        cache.evict(userId);

        User third = cache.getOrFetch(userId, () -> {
            dbCalls.incrementAndGet();
            User user = new User(userId);
            user.setEmail("reloaded.user@sourcebase.local");
            return user;
        });

        assertThat(first.getEmail()).isEqualTo("cached.user@sourcebase.local");
        assertThat(second.getEmail()).isEqualTo("cached.user@sourcebase.local");
        assertThat(third.getEmail()).isEqualTo("reloaded.user@sourcebase.local");
        assertThat(dbCalls).hasValue(2);
    }

}
