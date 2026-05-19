package com.vtlamdev.sourcebase.dao.service;

import com.vtlamdev.sourcebase.common.data.Tenant;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

public interface TenantService {
    boolean existsByEmail(@Nullable String email);

    Optional<Tenant> findTenantById(TenantId tenantId);

    Optional<Tenant> findByEmail(String email);

    Tenant saveTenant(Tenant tenant);
}
