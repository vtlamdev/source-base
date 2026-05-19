package com.vtlamdev.sourcebase.dao.service.impl;

import com.vtlamdev.sourcebase.common.data.EntityType;
import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.PageData;
import com.vtlamdev.sourcebase.common.data.page.PageLink;
import com.vtlamdev.sourcebase.dao.DaoUtil;
import com.vtlamdev.sourcebase.dao.JpaAbstractDao;
import com.vtlamdev.sourcebase.dao.model.sql.UserEntity;
import com.vtlamdev.sourcebase.dao.repository.UserRepository;
import com.vtlamdev.sourcebase.dao.service.UserDao;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserDao extends JpaAbstractDao<UserEntity, User> implements UserDao {

    private final UserRepository repository;

    public JpaUserDao(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<UserEntity, UUID> getRepository() {
        return repository;
    }

    @Override
    protected Class<UserEntity> getEntityClass() {
        return UserEntity.class;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.USER;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return DaoUtil.getOptionalData(repository.findByEmail(email));
    }

    @Override
    public Optional<User> findByTenantIdAndEmail(TenantId tenantId, String email) {
        return DaoUtil.getOptionalData(repository.findByTenantIdAndEmail(tenantId.getId(), email));
    }

    @Override
    public PageData<User> findAllByTenantId(TenantId tenantId, PageLink pageLink) {
        return DaoUtil.toPageData(repository.findByTenantId(tenantId.getId(), PageRequest.of(pageLink.getPage(), pageLink.getPageSize())));
    }

    @Override
    public Long countByTenantId(TenantId tenantId) {
        return repository.countByTenantId(tenantId.getId());
    }

}
