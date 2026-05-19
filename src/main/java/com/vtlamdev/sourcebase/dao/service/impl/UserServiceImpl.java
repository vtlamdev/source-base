package com.vtlamdev.sourcebase.dao.service.impl;

import com.vtlamdev.sourcebase.cache.UserProfileCache;
import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import com.vtlamdev.sourcebase.dao.service.UserDao;
import com.vtlamdev.sourcebase.dao.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final UserProfileCache userProfileCache;

    public UserServiceImpl(UserDao userDao, UserProfileCache userProfileCache) {
        this.userDao = userDao;
        this.userProfileCache = userProfileCache;
    }

    @Override
    public Optional<User> findUserById(TenantId tenantId, UserId userId) {
        return Optional.ofNullable(userDao.findById(tenantId, userId.getId()));
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public Optional<User> findUserByEmail(TenantId tenantId, String email) {
        return userDao.findByTenantIdAndEmail(tenantId, email);
    }

    @Override
    public User saveUser(TenantId tenantId, User user) {
        UserId existingUserId = user.getId();
        User savedUser = userDao.save(tenantId, user);
        userProfileCache.evict(existingUserId);
        userProfileCache.evict(savedUser.getId());
        return savedUser;
    }

}
