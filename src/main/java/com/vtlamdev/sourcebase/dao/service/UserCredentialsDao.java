package com.vtlamdev.sourcebase.dao.service;

import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import com.vtlamdev.sourcebase.common.data.security.UserCredentials;
import com.vtlamdev.sourcebase.dao.Dao;

import java.util.Optional;
import java.util.UUID;

public interface UserCredentialsDao extends Dao<UserCredentials> {

    Optional<UserCredentials> findByUserId(TenantId tenantId, UUID userId);

    Optional<UserCredentials> findByActivateToken(TenantId tenantId, String activateToken);

    Optional<UserCredentials> findByResetToken(TenantId tenantId, String resetToken);

    void removeByUserId(TenantId tenantId, UserId userId);

    void setLastLoginTs(TenantId tenantId, UserId userId, long lastLoginTs);

    void setFailedLoginAttempts(TenantId tenantId, UserId userId, int failedLoginAttempts);

}
