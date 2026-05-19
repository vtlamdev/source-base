package com.vtlamdev.sourcebase.service.impl;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.ListData;
import com.vtlamdev.sourcebase.common.data.page.ListQuery;
import com.vtlamdev.sourcebase.dao.service.AdminSettingsService;
import com.vtlamdev.sourcebase.service.AdminSettingsApplicationService;
import org.springframework.stereotype.Service;

@Service
public class AdminSettingsApplicationServiceImpl implements AdminSettingsApplicationService {

    private final AdminSettingsService adminSettingsService;

    public AdminSettingsApplicationServiceImpl(AdminSettingsService adminSettingsService) {
        this.adminSettingsService = adminSettingsService;
    }

    @Override
    public AdminSettings getByKey(TenantId tenantId, String key) {
        return adminSettingsService.findAdminSettingsByKey(tenantId, key);
    }

    @Override
    public ListData<AdminSettings> getAll(TenantId tenantId, ListQuery listQuery) {
        return adminSettingsService.findAllByTenantId(tenantId, listQuery);
    }

    @Override
    public AdminSettings save(TenantId tenantId, AdminSettings adminSettings) {
        return adminSettingsService.saveAdminSettings(tenantId, adminSettings);
    }

    @Override
    public boolean delete(TenantId tenantId, String key) {
        return adminSettingsService.deleteAdminSettingsByTenantIdAndKey(tenantId, key);
    }

}
