package com.vtlamdev.sourcebase.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SpringTbTransactionalCache<K extends Serializable, V extends Serializable> implements TbTransactionalCache<K, V> {

    private final String cacheName;
    private final Cache cache;
    private final Lock lock = new ReentrantLock();
    private final Map<K, Set<UUID>> objectTransactions = new HashMap<>();
    private final Map<UUID, SpringTbCacheTransaction<K, V>> transactions = new HashMap<>();

    public SpringTbTransactionalCache(CacheManager cacheManager, String cacheName) {
        this.cacheName = cacheName;
        this.cache = Optional.ofNullable(cacheManager.getCache(cacheName))
                .orElseThrow(() -> new IllegalArgumentException("Cache '" + cacheName + "' is not configured"));
    }

    @Override
    public String getCacheName() {
        return cacheName;
    }

    @Override
    public TbCacheValueWrapper<V> get(K key) {
        return SimpleTbCacheValueWrapper.wrap(cache.get(key));
    }

    @Override
    public void put(K key, V value) {
        lock.lock();
        try {
            failAllTransactionsByKey(key);
            cache.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void putIfAbsent(K key, V value) {
        lock.lock();
        try {
            failAllTransactionsByKey(key);
            doPutIfAbsent(key, value);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void evict(K key) {
        lock.lock();
        try {
            failAllTransactionsByKey(key);
            doEvict(key);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void evict(Collection<K> keys) {
        lock.lock();
        try {
            for (K key : keys) {
                failAllTransactionsByKey(key);
                doEvict(key);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void evictOrPut(K key, V value) {
        lock.lock();
        try {
            failAllTransactionsByKey(key);
            Cache.ValueWrapper existingValue = cache.get(key);
            doEvict(key);
            if (existingValue == null) {
                doPutIfAbsent(key, value);
            }
        } finally {
            lock.unlock();
        }
    }

    @Override
    public TbCacheTransaction<K, V> newTransactionForKey(K key) {
        return newTransaction(Collections.singletonList(key));
    }

    @Override
    public TbCacheTransaction<K, V> newTransactionForKeys(List<K> keys) {
        return newTransaction(keys);
    }

    void doPutIfAbsent(K key, V value) {
        cache.putIfAbsent(key, value);
    }

    void doEvict(K key) {
        cache.evict(key);
    }

    TbCacheTransaction<K, V> newTransaction(List<K> keys) {
        lock.lock();
        try {
            SpringTbCacheTransaction<K, V> transaction = new SpringTbCacheTransaction<>(this, keys);
            UUID transactionId = transaction.getId();
            for (K key : keys) {
                objectTransactions.computeIfAbsent(key, unused -> new HashSet<>()).add(transactionId);
            }
            transactions.put(transactionId, transaction);
            return transaction;
        } finally {
            lock.unlock();
        }
    }

    boolean commit(UUID transactionId, Map<K, V> pendingPuts) {
        lock.lock();
        try {
            SpringTbCacheTransaction<K, V> transaction = transactions.get(transactionId);
            boolean success = transaction != null && !transaction.isFailed();
            if (success) {
                for (K key : transaction.getKeys()) {
                    Set<UUID> otherTransactions = objectTransactions.get(key);
                    if (otherTransactions != null) {
                        for (UUID otherTransactionId : otherTransactions) {
                            if (!transactionId.equals(otherTransactionId)) {
                                SpringTbCacheTransaction<K, V> otherTransaction = transactions.get(otherTransactionId);
                                if (otherTransaction != null) {
                                    otherTransaction.setFailed(true);
                                }
                            }
                        }
                    }
                }
                pendingPuts.forEach(this::doPutIfAbsent);
            }
            removeTransaction(transactionId);
            return success;
        } finally {
            lock.unlock();
        }
    }

    void rollback(UUID transactionId) {
        lock.lock();
        try {
            removeTransaction(transactionId);
        } finally {
            lock.unlock();
        }
    }

    private void removeTransaction(UUID transactionId) {
        SpringTbCacheTransaction<K, V> transaction = transactions.remove(transactionId);
        if (transaction == null) {
            return;
        }
        for (K key : transaction.getKeys()) {
            Set<UUID> transactionIds = objectTransactions.get(key);
            if (transactionIds != null) {
                transactionIds.remove(transactionId);
                if (transactionIds.isEmpty()) {
                    objectTransactions.remove(key);
                }
            }
        }
    }

    private void failAllTransactionsByKey(K key) {
        Set<UUID> transactionIds = objectTransactions.get(key);
        if (transactionIds == null) {
            return;
        }
        for (UUID transactionId : transactionIds) {
            SpringTbCacheTransaction<K, V> transaction = transactions.get(transactionId);
            if (transaction != null) {
                transaction.setFailed(true);
            }
        }
    }

}
