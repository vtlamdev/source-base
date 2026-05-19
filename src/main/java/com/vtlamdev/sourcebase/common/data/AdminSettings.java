package com.vtlamdev.sourcebase.common.data;

import com.vtlamdev.sourcebase.common.data.id.AdminSettingsId;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import io.swagger.v3.oas.annotations.media.Schema;
import tools.jackson.databind.JsonNode;

@Schema
public class AdminSettings extends BaseData<AdminSettingsId> implements HasTenantId {

    private TenantId tenantId;
    private String key;
    private JsonNode jsonValue;

    public AdminSettings() {
    }

    public AdminSettings(AdminSettingsId id) {
        super(id);
    }

    public AdminSettings(AdminSettings adminSettings) {
        super(adminSettings);
        this.tenantId = adminSettings.getTenantId();
        this.key = adminSettings.getKey();
        this.jsonValue = adminSettings.getJsonValue();
    }

    @Override
    public TenantId getTenantId() {
        return tenantId;
    }

    public void setTenantId(TenantId tenantId) {
        this.tenantId = tenantId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JsonNode getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(JsonNode jsonValue) {
        this.jsonValue = jsonValue;
    }

}
