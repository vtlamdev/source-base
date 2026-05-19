package com.vtlamdev.sourcebase.dao;

import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.PageData;
import com.vtlamdev.sourcebase.common.data.page.PageLink;

public interface TenantEntityDao<T> {

    default Long countByTenantId(TenantId tenantId) {
        throw new UnsupportedOperationException();
    }

    default PageData<T> findAllByTenantId(TenantId tenantId, PageLink pageLink) {
        throw new UnsupportedOperationException();
    }

}
