package com.vtlamdev.sourcebase.cache;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class TbTransactionalCacheFactory {

    private final CacheManager cacheManager;

    public TbTransactionalCacheFactory(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public <K extends Serializable, V extends Serializable> TbTransactionalCache<K, V> createCache(String cacheName) {
        return new SpringTbTransactionalCache<>(cacheManager, cacheName);
    }

}
