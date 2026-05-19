package com.vtlamdev.sourcebase.dao;

import com.vtlamdev.sourcebase.common.data.EntityType;
import com.vtlamdev.sourcebase.common.data.id.TenantId;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface Dao<T> {

    List<T> find(TenantId tenantId);

    T findById(TenantId tenantId, UUID id);

    CompletableFuture<T> findByIdAsync(TenantId tenantId, UUID id);

    boolean existsById(TenantId tenantId, UUID id);

    CompletableFuture<Boolean> existsByIdAsync(TenantId tenantId, UUID id);

    T save(TenantId tenantId, T t);

    T saveAndFlush(TenantId tenantId, T t);

    void removeById(TenantId tenantId, UUID id);

    void removeAllByIds(Collection<UUID> ids);

    List<UUID> findIdsByTenantIdAndIdOffset(TenantId tenantId, UUID idOffset, int limit);

    default EntityType getEntityType() {
        return null;
    }

}
