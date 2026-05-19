package com.vtlamdev.sourcebase.common.data.id;

import com.vtlamdev.sourcebase.common.data.EntityType;

import java.util.UUID;

public class UserId extends UUIDBased implements EntityId {

    public UserId(UUID id) {
        super(id);
    }

    public static UserId fromString(String userId) {
        return new UserId(UUID.fromString(userId));
    }

    @Override
    public EntityType getEntityType() {
        return EntityType.USER;
    }

}
