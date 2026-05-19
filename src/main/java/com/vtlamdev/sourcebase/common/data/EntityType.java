package com.vtlamdev.sourcebase.common.data;

import java.util.Arrays;

public enum EntityType {

    TENANT(1, "tenant"),
    CUSTOMER(2, "customer"),
    USER(3, "tb_user"),
    DASHBOARD(4, "dashboard"),
    ASSET(5, "asset"),
    DEVICE(6, "device"),
    RULE_CHAIN(11, "rule_chain"),
    RULE_NODE(12, "rule_node"),
    TENANT_PROFILE(20, "tenant_profile"),
    DEVICE_PROFILE(21, "device_profile"),
    ASSET_PROFILE(22, "asset_profile"),
    OTA_PACKAGE(25, "ota_package"),
    QUEUE(28, "queue"),
    NOTIFICATION(32, "notification"),
    ADMIN_SETTINGS(42, "admin_settings"),
    USER_CREDENTIALS(45, "user_credentials");

    private final int protoNumber;
    private final String tableName;

    EntityType(int protoNumber, String tableName) {
        this.protoNumber = protoNumber;
        this.tableName = tableName;
    }

    public int getProtoNumber() {
        return protoNumber;
    }

    public String getTableName() {
        return tableName;
    }

    public boolean isOneOf(EntityType... types) {
        if (types == null) {
            return false;
        }
        return Arrays.stream(types).anyMatch(type -> type == this);
    }

}
