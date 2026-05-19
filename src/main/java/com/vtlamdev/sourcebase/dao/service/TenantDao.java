package com.vtlamdev.sourcebase.dao.service;

import com.vtlamdev.sourcebase.common.data.Tenant;
import com.vtlamdev.sourcebase.dao.Dao;
import com.vtlamdev.sourcebase.dao.TenantEntityDao;

import java.util.Optional;

public interface TenantDao extends Dao<Tenant>, TenantEntityDao<Tenant> {
    boolean existsByEmail(String email);

    Optional<Tenant> findByEmail(String email);
}
