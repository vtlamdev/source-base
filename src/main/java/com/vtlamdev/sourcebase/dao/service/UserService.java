package com.vtlamdev.sourcebase.dao.service;

import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.id.UserId;

import java.util.Optional;

public interface UserService {

    Optional<User> findUserById(TenantId tenantId, UserId userId);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByEmail(TenantId tenantId, String email);

    User saveUser(TenantId tenantId, User user);

}
