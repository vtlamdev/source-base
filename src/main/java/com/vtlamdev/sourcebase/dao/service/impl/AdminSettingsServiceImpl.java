package com.vtlamdev.sourcebase.dao.service.impl;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.id.AdminSettingsId;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.page.ListData;
import com.vtlamdev.sourcebase.common.data.page.ListQuery;
import com.vtlamdev.sourcebase.common.data.page.PageData;
import com.vtlamdev.sourcebase.common.data.page.ScrollData;
import com.vtlamdev.sourcebase.dao.service.AdminSettingsDao;
import com.vtlamdev.sourcebase.dao.service.AdminSettingsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AdminSettingsServiceImpl implements AdminSettingsService {

    private final AdminSettingsDao adminSettingsDao;

    public AdminSettingsServiceImpl(AdminSettingsDao adminSettingsDao) {
        this.adminSettingsDao = adminSettingsDao;
    }

    @Override
    public AdminSettings findAdminSettingsById(TenantId tenantId, AdminSettingsId adminSettingsId) {
        if (adminSettingsId == null) {
            throw new IllegalArgumentException("adminSettingsId is required");
        }
        return adminSettingsDao.findById(tenantId, adminSettingsId.getId());
    }

    @Override
    public AdminSettings findAdminSettingsByKey(TenantId tenantId, String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("key is required");
        }
        return adminSettingsDao.findByTenantIdAndKey(tenantId.getId(), key);
    }

    @Override
    public ListData<AdminSettings> findAllByTenantId(TenantId tenantId, ListQuery listQuery) {
        if (listQuery.isScrollMode()) {
            ScrollData<AdminSettings> scrollData = adminSettingsDao.findScrollByTenantId(tenantId, listQuery);
            return ListData.scroll(scrollData.getData(), scrollData.getLimit(), scrollData.getNextCursor(), scrollData.isHasNext());
        }
        PageData<AdminSettings> pageData = adminSettingsDao.findAllByTenantId(tenantId, listQuery.toPageLink());
        return ListData.page(pageData.getData(),
                listQuery.getPage(),
                listQuery.getPageSize(),
                pageData.getTotalPages(),
                pageData.getTotalElements(),
                pageData.isHasNext());
    }

    @Override
    public AdminSettings saveAdminSettings(TenantId tenantId, AdminSettings adminSettings) {
        if (adminSettings == null) {
            throw new IllegalArgumentException("adminSettings is required");
        }
        if (!StringUtils.hasText(adminSettings.getKey())) {
            throw new IllegalArgumentException("adminSettings.key is required");
        }
        if (adminSettings.getTenantId() == null) {
            adminSettings.setTenantId(tenantId != null ? tenantId : TenantId.SYS_TENANT_ID);
        }
        return adminSettingsDao.save(tenantId, adminSettings);
    }

    @Override
    public boolean deleteAdminSettingsByTenantIdAndKey(TenantId tenantId, String key) {
        if (!StringUtils.hasText(key)) {
            throw new IllegalArgumentException("key is required");
        }
        return adminSettingsDao.removeByTenantIdAndKey(tenantId.getId(), key);
    }

}
