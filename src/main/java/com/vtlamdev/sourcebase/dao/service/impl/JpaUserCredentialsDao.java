package com.vtlamdev.sourcebase.dao.service.impl;

import com.vtlamdev.sourcebase.common.data.EntityType;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import com.vtlamdev.sourcebase.common.data.security.UserCredentials;
import com.vtlamdev.sourcebase.dao.JpaAbstractDao;
import com.vtlamdev.sourcebase.dao.model.sql.UserCredentialsEntity;
import com.vtlamdev.sourcebase.dao.repository.UserCredentialsRepository;
import com.vtlamdev.sourcebase.dao.service.UserCredentialsDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class JpaUserCredentialsDao extends JpaAbstractDao<UserCredentialsEntity, UserCredentials> implements UserCredentialsDao {

    private final UserCredentialsRepository repository;

    public JpaUserCredentialsDao(UserCredentialsRepository repository) {
        this.repository = repository;
    }

    @Override
    protected JpaRepository<UserCredentialsEntity, UUID> getRepository() {
        return repository;
    }

    @Override
    protected Class<UserCredentialsEntity> getEntityClass() {
        return UserCredentialsEntity.class;
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.USER_CREDENTIALS;
    }

    @Override
    public Optional<UserCredentials> findByUserId(TenantId tenantId, UUID userId) {
        return repository.findByUserId(userId).map(UserCredentialsEntity::toData);
    }

    @Override
    public Optional<UserCredentials> findByActivateToken(TenantId tenantId, String activateToken) {
        return repository.findByActivateToken(activateToken).map(UserCredentialsEntity::toData);
    }

    @Override
    public Optional<UserCredentials> findByResetToken(TenantId tenantId, String resetToken) {
        return repository.findByResetToken(resetToken).map(UserCredentialsEntity::toData);
    }

    @Override
    public void removeByUserId(TenantId tenantId, UserId userId) {
        repository.deleteByUserId(userId.getId());
    }

    @Override
    public void setLastLoginTs(TenantId tenantId, UserId userId, long lastLoginTs) {
        repository.updateLastLoginTsByUserId(userId.getId(), lastLoginTs);
    }

    @Override
    public void setFailedLoginAttempts(TenantId tenantId, UserId userId, int failedLoginAttempts) {
        repository.updateFailedLoginAttemptsByUserId(userId.getId(), failedLoginAttempts);
    }

}
