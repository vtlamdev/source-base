package com.vtlamdev.sourcebase.cache;

import org.springframework.cache.Cache;

public final class SimpleTbCacheValueWrapper<T> implements TbCacheValueWrapper<T> {

    private final T value;

    private SimpleTbCacheValueWrapper(T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    public static <T> SimpleTbCacheValueWrapper<T> empty() {
        return new SimpleTbCacheValueWrapper<>(null);
    }

    public static <T> SimpleTbCacheValueWrapper<T> wrap(T value) {
        return new SimpleTbCacheValueWrapper<>(value);
    }

    @SuppressWarnings("unchecked")
    public static <T> SimpleTbCacheValueWrapper<T> wrap(Cache.ValueWrapper source) {
        return source == null ? null : new SimpleTbCacheValueWrapper<>((T) source.get());
    }

}
