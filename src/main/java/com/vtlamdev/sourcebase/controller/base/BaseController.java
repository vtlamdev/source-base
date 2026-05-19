package com.vtlamdev.sourcebase.controller.base;

import com.vtlamdev.sourcebase.common.data.id.TenantId;

import java.util.UUID;

public abstract class BaseController {

    protected TenantId systemTenantId() {
        return TenantId.SYS_TENANT_ID;
    }

    protected TenantId tenantId(String tenantId) {
        return tenantId == null || tenantId.isBlank() ? TenantId.SYS_TENANT_ID : TenantId.fromUUID(UUID.fromString(tenantId));
    }

}
