package com.vtlamdev.sourcebase.controller;

import com.vtlamdev.sourcebase.common.data.Tenant;
import com.vtlamdev.sourcebase.controller.base.BaseController;
import com.vtlamdev.sourcebase.security.SecurityContextUtils;
import com.vtlamdev.sourcebase.service.TenantService;
import com.vtlamdev.sourcebase.service.security.model.SecurityUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenant")
@Tag(name = "Tenant")
public class TenantController extends BaseController {

    private final TenantService tenantService;

    public TenantController(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping("/register")
    public Tenant register(@RequestBody Tenant tenant) {
        return tenantService.registerCurrentUserTenant(requireCurrentUser(), tenant);
    }

    private SecurityUser requireCurrentUser() {
        SecurityUser currentUser = SecurityContextUtils.currentUser();
        if (currentUser == null) {
            throw new BadCredentialsException("No authenticated user");
        }
        return currentUser;
    }
}
