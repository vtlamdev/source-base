package com.vtlamdev.sourcebase.cache;

import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class UserProfileCache {

    private static final Logger log = LoggerFactory.getLogger(UserProfileCache.class);

    private final TbTransactionalCache<String, User> cache;

    public UserProfileCache(TbTransactionalCacheFactory cacheFactory) {
        this.cache = cacheFactory.createCache(CacheNames.USER_LOOKUP);
    }

    public User getOrFetch(UserId userId, Supplier<User> dbCall) {
        try {
            return cache.getOrFetchFromDB(toCacheKey(userId), dbCall, false, true);
        } catch (RuntimeException e) {
            log.warn("Failed to load user profile from cache for userId={}. Falling back to DB.", userId, e);
            return dbCall.get();
        }
    }

    public void evict(UserId userId) {
        if (userId != null) {
            try {
                cache.evict(toCacheKey(userId));
            } catch (RuntimeException e) {
                log.warn("Failed to evict user profile cache for userId={}", userId, e);
            }
        }
    }

    private String toCacheKey(UserId userId) {
        return userId.toString();
    }

}
