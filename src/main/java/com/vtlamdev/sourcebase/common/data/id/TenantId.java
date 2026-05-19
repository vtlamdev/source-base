package com.vtlamdev.sourcebase.common.data.id;

import com.vtlamdev.sourcebase.common.data.EntityType;

import java.util.UUID;

public final class TenantId extends UUIDBased implements EntityId {

    public static final TenantId SYS_TENANT_ID = new TenantId(EntityId.NULL_UUID);

    public static TenantId fromUUID(UUID id) {
        return new TenantId(id);
    }

    public TenantId(UUID id) {
        super(id);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.TENANT;
    }

}
