package com.vtlamdev.sourcebase.cache;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

final class SpringTbCacheTransaction<K extends Serializable, V extends Serializable> implements TbCacheTransaction<K, V> {

    private final UUID id = UUID.randomUUID();
    private final SpringTbTransactionalCache<K, V> cache;
    private final List<K> keys;
    private boolean failed;
    private final Map<K, V> pendingPuts = new LinkedHashMap<>();

    SpringTbCacheTransaction(SpringTbTransactionalCache<K, V> cache, List<K> keys) {
        this.cache = cache;
        this.keys = keys;
    }

    UUID getId() {
        return id;
    }

    List<K> getKeys() {
        return keys;
    }

    boolean isFailed() {
        return failed;
    }

    void setFailed(boolean failed) {
        this.failed = failed;
    }

    Map<K, V> getPendingPuts() {
        return pendingPuts;
    }

    @Override
    public void put(K key, V value) {
        pendingPuts.put(key, value);
    }

    @Override
    public boolean commit() {
        return cache.commit(id, pendingPuts);
    }

    @Override
    public void rollback() {
        cache.rollback(id);
    }

}
