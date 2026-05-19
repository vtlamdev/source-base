package com.vtlamdev.sourcebase.dao.service;

import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import com.vtlamdev.sourcebase.common.data.security.UserCredentials;

import java.util.Optional;

public interface UserCredentialsService {

    Optional<UserCredentials> findUserCredentialsByUserId(TenantId tenantId, UserId userId);

    UserCredentials saveUserCredentials(TenantId tenantId, UserCredentials userCredentials);

    UserCredentials replaceUserCredentials(TenantId tenantId, UserCredentials userCredentials);

    void setLastLoginTs(TenantId tenantId, UserId userId, long lastLoginTs);

    void setFailedLoginAttempts(TenantId tenantId, UserId userId, int failedLoginAttempts);

}
