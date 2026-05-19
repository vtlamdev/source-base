package com.vtlamdev.sourcebase.service.impl;

import com.vtlamdev.sourcebase.common.data.Tenant;
import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.security.Authority;
import com.vtlamdev.sourcebase.common.data.exception.SourceBaseErrorCode;
import com.vtlamdev.sourcebase.dao.service.UserService;
import com.vtlamdev.sourcebase.exception.ResourceNotFoundException;
import com.vtlamdev.sourcebase.exception.SourceBaseException;
import com.vtlamdev.sourcebase.service.TenantService;
import com.vtlamdev.sourcebase.service.security.model.SecurityUser;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service("tenantApplicationService")
public class TenantServiceImpl implements TenantService {
    private final com.vtlamdev.sourcebase.dao.service.TenantService tenantService;
    private final UserService userService;

    public TenantServiceImpl(com.vtlamdev.sourcebase.dao.service.TenantService tenantService, UserService userService) {
        this.tenantService = tenantService;
        this.userService = userService;
    }

    @Override
    public Tenant registerCurrentUserTenant(SecurityUser currentUser, Tenant request) {
        if (currentUser == null) {
            throw new SourceBaseException(SourceBaseErrorCode.UNAUTHORIZED, "Current user is required");
        }
        if (request == null) {
            throw new SourceBaseException(SourceBaseErrorCode.VALIDATION, "Tenant request is required");
        }
        if (!StringUtils.hasText(request.getEmail())) {
            throw new SourceBaseException(SourceBaseErrorCode.VALIDATION, "Tenant email is required");
        }
        User user = userService.findUserById(TenantId.SYS_TENANT_ID, currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        boolean isCreate = request.getId() == null || request.getId().getId() == null;
        Optional<Tenant> existingByEmail = tenantService.findByEmail(request.getEmail());

        if (isCreate) {
            if (existingByEmail.isPresent()) {
                throw new SourceBaseException(SourceBaseErrorCode.VALIDATION, "Email already exists");
            }
            if (!isUserWithoutTenant(user)) {
                throw new SourceBaseException(SourceBaseErrorCode.VALIDATION, "Current user is already attached to a tenant");
            }

            Tenant tenant = tenantService.saveTenant(request);
            user.setTenantId(tenant.getId());
            user.setAuthority(Authority.TENANT_ADMIN);
            userService.saveUser(TenantId.SYS_TENANT_ID, user);
            return tenant;
        }

        Tenant currentTenant = user.getTenantId() == null ? null : tenantService.findTenantById(user.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Current tenant not found"));
        if (currentTenant == null) {
            throw new SourceBaseException(SourceBaseErrorCode.VALIDATION, "Current user does not own a tenant to update");
        }
        if (!currentTenant.getId().getId().equals(request.getId().getId())) {
            throw new SourceBaseException(SourceBaseErrorCode.UNAUTHORIZED, "Cannot update another tenant");
        }
        if (existingByEmail.isPresent() && !existingByEmail.get().getId().getId().equals(request.getId().getId())) {
            throw new SourceBaseException(SourceBaseErrorCode.VALIDATION, "Email already exists");
        }
        return tenantService.saveTenant(request);
    }

    private boolean isUserWithoutTenant(User user) {
        return user.getTenantId() == null || TenantId.SYS_TENANT_ID.getId().equals(user.getTenantId().getId());
    }
}
