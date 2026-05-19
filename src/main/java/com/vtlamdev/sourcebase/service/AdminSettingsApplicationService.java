package com.vtlamdev.sourcebase.service;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.ListData;
import com.vtlamdev.sourcebase.common.data.page.ListQuery;

public interface AdminSettingsApplicationService {

    AdminSettings getByKey(TenantId tenantId, String key);

    ListData<AdminSettings> getAll(TenantId tenantId, ListQuery listQuery);

    AdminSettings save(TenantId tenantId, AdminSettings adminSettings);

    boolean delete(TenantId tenantId, String key);

}
