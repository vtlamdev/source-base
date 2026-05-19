package com.vtlamdev.sourcebase.common.data.id;

import com.vtlamdev.sourcebase.common.data.EntityType;

import java.util.UUID;

public final class CustomerId extends UUIDBased implements EntityId {

    public CustomerId(UUID id) {
        super(id);
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.CUSTOMER;
    }

}
