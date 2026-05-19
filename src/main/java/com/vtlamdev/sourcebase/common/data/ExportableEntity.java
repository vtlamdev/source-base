package com.vtlamdev.sourcebase.common.data;

import com.vtlamdev.sourcebase.common.data.id.EntityId;
import com.vtlamdev.sourcebase.common.data.id.HasId;

import java.util.UUID;

/**
 * Marker contract for entities that can participate in import/export flows.
 * The current base source does not use it yet, but the interface mirrors the
 * capability-oriented style from ThingsBoard DAO design.
 */
public interface ExportableEntity<I extends EntityId> extends HasId<I>, HasName {

    UUID getExternalId();

}
