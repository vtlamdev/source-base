package com.vtlamdev.sourcebase.dao.service.impl;

import com.vtlamdev.sourcebase.common.data.EntityType;
import com.vtlamdev.sourcebase.common.data.Tenant;
import com.vtlamdev.sourcebase.dao.JpaAbstractDao;
import com.vtlamdev.sourcebase.dao.model.sql.TenantEntity;
import com.vtlamdev.sourcebase.dao.repository.TenantRepository;
import com.vtlamdev.sourcebase.dao.service.TenantDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaTenantDao extends JpaAbstractDao<TenantEntity, Tenant> implements TenantDao {

    private final TenantRepository tenantRepository;

    public JpaTenantDao(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @Override
    protected JpaRepository<TenantEntity, UUID> getRepository() {
        return tenantRepository;
    }

    @Override
    protected Class<TenantEntity> getEntityClass() {
        return TenantEntity.class;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.TENANT;
    }

    @Override
    public boolean existsByEmail(String email) {
        return tenantRepository.existsByEmail(email);
    }

    @Override
    public Optional<Tenant> findByEmail(String email) {
        return tenantRepository.findByEmail(email).map(TenantEntity::toData);
    }
}
