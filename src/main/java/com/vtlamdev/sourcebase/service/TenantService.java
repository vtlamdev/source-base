package com.vtlamdev.sourcebase.service;

import com.vtlamdev.sourcebase.common.data.Tenant;
import com.vtlamdev.sourcebase.service.security.model.SecurityUser;

public interface TenantService {

    Tenant registerCurrentUserTenant(SecurityUser currentUser, Tenant request);
}
