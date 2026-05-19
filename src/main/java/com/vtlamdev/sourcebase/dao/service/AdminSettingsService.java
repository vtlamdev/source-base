package com.vtlamdev.sourcebase.dao.service;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.id.AdminSettingsId;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.ListData;
import com.vtlamdev.sourcebase.common.data.page.ListQuery;

public interface AdminSettingsService {

    AdminSettings findAdminSettingsById(TenantId tenantId, AdminSettingsId adminSettingsId);

    AdminSettings findAdminSettingsByKey(TenantId tenantId, String key);

    ListData<AdminSettings> findAllByTenantId(TenantId tenantId, ListQuery listQuery);

    AdminSettings saveAdminSettings(TenantId tenantId, AdminSettings adminSettings);

    boolean deleteAdminSettingsByTenantIdAndKey(TenantId tenantId, String key);

}
