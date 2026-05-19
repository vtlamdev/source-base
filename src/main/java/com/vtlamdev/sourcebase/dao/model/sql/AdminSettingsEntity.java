package com.vtlamdev.sourcebase.dao.model.sql;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.id.AdminSettingsId;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.dao.DaoUtil;
import com.vtlamdev.sourcebase.dao.model.ModelConstants;
import com.vtlamdev.sourcebase.dao.model.SoftDeletableEntity;
import com.vtlamdev.sourcebase.dao.util.mapping.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Entity
@Table(name = ModelConstants.ADMIN_SETTINGS_TABLE_NAME)
public class AdminSettingsEntity extends SoftDeletableEntity<AdminSettings> {

    @Column(name = ModelConstants.TENANT_ID_PROPERTY, nullable = false)
    private UUID tenantId;

    @Column(name = ModelConstants.ADMIN_SETTINGS_KEY_PROPERTY, nullable = false)
    private String key;

    @Convert(converter = JsonConverter.class)
    @Column(name = ModelConstants.ADMIN_SETTINGS_JSON_VALUE_PROPERTY, columnDefinition = "text")
    private JsonNode jsonValue;

    public AdminSettingsEntity() {
    }

    public AdminSettingsEntity(AdminSettings adminSettings) {
        super(adminSettings);
        this.tenantId = DaoUtil.getId(adminSettings.getTenantId()) != null ? DaoUtil.getId(adminSettings.getTenantId()) : TenantId.SYS_TENANT_ID.getId();
        this.key = adminSettings.getKey();
        this.jsonValue = adminSettings.getJsonValue();
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public String getKey() {
        return key;
    }

    public JsonNode getJsonValue() {
        return jsonValue;
    }

    @Override
    public AdminSettings toData() {
        AdminSettings adminSettings = new AdminSettings(new AdminSettingsId(id));
        adminSettings.setCreatedTime(createdTime);
        adminSettings.setTenantId(DaoUtil.toEntityId(tenantId, TenantId::fromUUID));
        adminSettings.setKey(key);
        adminSettings.setJsonValue(jsonValue);
        return adminSettings;
    }

}
