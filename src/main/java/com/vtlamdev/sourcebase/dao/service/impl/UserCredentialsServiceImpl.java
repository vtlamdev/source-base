package com.vtlamdev.sourcebase.dao.service.impl;

import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import com.vtlamdev.sourcebase.common.data.security.UserCredentials;
import com.vtlamdev.sourcebase.dao.service.UserCredentialsDao;
import com.vtlamdev.sourcebase.dao.service.UserCredentialsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCredentialsServiceImpl implements UserCredentialsService {

    private final UserCredentialsDao userCredentialsDao;

    public UserCredentialsServiceImpl(UserCredentialsDao userCredentialsDao) {
        this.userCredentialsDao = userCredentialsDao;
    }

    @Override
    public Optional<UserCredentials> findUserCredentialsByUserId(TenantId tenantId, UserId userId) {
        return userCredentialsDao.findByUserId(tenantId, userId.getId());
    }

    @Override
    public UserCredentials saveUserCredentials(TenantId tenantId, UserCredentials userCredentials) {
        return userCredentialsDao.save(tenantId, userCredentials);
    }

    @Override
    public UserCredentials replaceUserCredentials(TenantId tenantId, UserCredentials userCredentials) {
        return userCredentialsDao.saveAndFlush(tenantId, userCredentials);
    }

    @Override
    public void setLastLoginTs(TenantId tenantId, UserId userId, long lastLoginTs) {
        userCredentialsDao.setLastLoginTs(tenantId, userId, lastLoginTs);
    }

    @Override
    public void setFailedLoginAttempts(TenantId tenantId, UserId userId, int failedLoginAttempts) {
        userCredentialsDao.setFailedLoginAttempts(tenantId, userId, failedLoginAttempts);
    }

}
