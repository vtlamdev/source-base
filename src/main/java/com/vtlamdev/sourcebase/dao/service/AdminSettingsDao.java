package com.vtlamdev.sourcebase.dao.service;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.ListQuery;
import com.vtlamdev.sourcebase.common.data.page.PageData;
import com.vtlamdev.sourcebase.common.data.page.PageLink;
import com.vtlamdev.sourcebase.common.data.page.ScrollData;
import com.vtlamdev.sourcebase.dao.Dao;
import com.vtlamdev.sourcebase.dao.TenantEntityDao;

import java.util.UUID;

public interface AdminSettingsDao extends Dao<AdminSettings>, TenantEntityDao<AdminSettings> {

    AdminSettings findByTenantIdAndKey(UUID tenantId, String key);

    @Override
    PageData<AdminSettings> findAllByTenantId(TenantId tenantId, PageLink pageLink);

    @Override
    Long countByTenantId(TenantId tenantId);

    @Override
    AdminSettings findById(TenantId tenantId, UUID id);

    ScrollData<AdminSettings> findScrollByTenantId(TenantId tenantId, ListQuery listQuery);

    boolean removeByTenantIdAndKey(UUID tenantId, String key);

    void removeByTenantId(UUID tenantId);

}
