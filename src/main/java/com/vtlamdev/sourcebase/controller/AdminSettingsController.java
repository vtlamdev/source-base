package com.vtlamdev.sourcebase.controller;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.page.ListData;
import com.vtlamdev.sourcebase.common.data.page.ListMode;
import com.vtlamdev.sourcebase.common.data.page.ListQuery;
import com.vtlamdev.sourcebase.controller.base.BaseController;
import com.vtlamdev.sourcebase.service.AdminSettingsApplicationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/settings")
@Tag(name = "Admin Settings")
public class AdminSettingsController extends BaseController {

    private final AdminSettingsApplicationService adminSettingsApplicationService;

    public AdminSettingsController(AdminSettingsApplicationService adminSettingsApplicationService) {
        this.adminSettingsApplicationService = adminSettingsApplicationService;
    }

    @GetMapping("/{key}")
    public AdminSettings getByKey(@PathVariable String key, @RequestParam(required = false) String tenantId) {
        return adminSettingsApplicationService.getByKey(tenantId(tenantId), key);
    }

    @GetMapping
    public ListData<AdminSettings> getAll(@RequestParam(defaultValue = "PAGE") String mode,
                                          @RequestParam(defaultValue = "20") int pageSize,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "20") int limit,
                                          @RequestParam(required = false) String cursor,
                                          @RequestParam(required = false) String textSearch,
                                          @RequestParam(required = false) String tenantId) {
        return adminSettingsApplicationService.getAll(tenantId(tenantId),
                new ListQuery(ListMode.fromValue(mode), page, pageSize, limit, textSearch, cursor));
    }

    @PostMapping
    public AdminSettings save(@RequestBody AdminSettings adminSettings, @RequestParam(required = false) String tenantId) {
        return adminSettingsApplicationService.save(tenantId(tenantId), adminSettings);
    }

    @DeleteMapping("/{key}")
    public boolean delete(@PathVariable String key, @RequestParam(required = false) String tenantId) {
        return adminSettingsApplicationService.delete(tenantId(tenantId), key);
    }

}
