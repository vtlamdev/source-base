package com.vtlamdev.sourcebase.dao;

import com.vtlamdev.sourcebase.common.data.ExportableEntity;
import com.vtlamdev.sourcebase.common.data.id.EntityId;
import com.vtlamdev.sourcebase.common.data.page.PageData;
import com.vtlamdev.sourcebase.common.data.page.PageLink;

import java.util.UUID;

public interface ExportableEntityDao<I extends EntityId, T extends ExportableEntity<I>> extends Dao<T> {

    T findByTenantIdAndExternalId(UUID tenantId, UUID externalId);

    default T findByTenantIdAndName(UUID tenantId, String name) {
        throw new UnsupportedOperationException();
    }

    PageData<T> findByTenantId(UUID tenantId, PageLink pageLink);

    default PageData<I> findIdsByTenantId(UUID tenantId, PageLink pageLink) {
        return findByTenantId(tenantId, pageLink).mapData(ExportableEntity::getId);
    }

    I getExternalIdByInternal(I internalId);

    default T findDefaultEntityByTenantId(UUID tenantId) {
        throw new UnsupportedOperationException();
    }

}
