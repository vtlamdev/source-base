package com.vtlamdev.sourcebase.common.data.id;

import com.vtlamdev.sourcebase.common.data.EntityType;

import java.io.Serializable;
import java.util.UUID;

public interface EntityId extends HasUUID, Serializable {

    UUID NULL_UUID = UUID.fromString("13814000-1dd2-11b2-8080-808080808080");

    EntityType getEntityType();

    default boolean isNullUid() {
        return NULL_UUID.equals(getId());
    }

}
