package com.vtlamdev.sourcebase.common.data.id;

import com.vtlamdev.sourcebase.common.data.EntityType;

import java.util.UUID;

public class AdminSettingsId extends UUIDBased implements EntityId {

    public AdminSettingsId(UUID id) {
        super(id);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.ADMIN_SETTINGS;
    }

}
