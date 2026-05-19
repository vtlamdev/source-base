package com.vtlamdev.sourcebase.dao.service.impl;

import com.vtlamdev.sourcebase.common.data.Tenant;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.dao.service.TenantDao;
import com.vtlamdev.sourcebase.dao.service.TenantService;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service("tenantDaoService")
public class TenantServiceImpl implements TenantService {

    private final TenantDao tenantDao;

    public TenantServiceImpl(TenantDao tenantDao) {
        this.tenantDao = tenantDao;
    }

    @Override
    public boolean existsByEmail(@Nullable String email) {
        if (!StringUtils.hasText(email)) {
            throw new IllegalArgumentException("email is required");
        }
        return tenantDao.existsByEmail(email.trim());
    }

    @Override
    public Optional<Tenant> findTenantById(TenantId tenantId) {
        if (tenantId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(tenantDao.findById(TenantId.SYS_TENANT_ID, tenantId.getId()));
    }

    @Override
    public Optional<Tenant> findByEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return Optional.empty();
        }
        return tenantDao.findByEmail(email.trim());
    }

    @Override
    public Tenant saveTenant(Tenant tenant) {
        if (tenant == null) {
            throw new IllegalArgumentException("tenant is required");
        }
        if (!StringUtils.hasText(tenant.getEmail())) {
            throw new IllegalArgumentException("tenant.email is required");
        }
        if (!StringUtils.hasText(tenant.getCountry())) {
            throw new IllegalArgumentException("tenant.country is required");
        }
        if (!StringUtils.hasText(tenant.getAddress())) {
            throw new IllegalArgumentException("tenant.address is required");
        }
        if (!StringUtils.hasText(tenant.getTitle())) {
            throw new IllegalArgumentException("tenant.title is required");
        }
        if (!StringUtils.hasText(tenant.getRegion())) {
            throw new IllegalArgumentException("tenant.region is required");
        }
        if (tenant.getState() == null) {
            throw new IllegalArgumentException("tenant.state is required");
        }

        tenant.setEmail(tenant.getEmail().trim());
        return tenantDao.save(TenantId.SYS_TENANT_ID, tenant);
    }
}
