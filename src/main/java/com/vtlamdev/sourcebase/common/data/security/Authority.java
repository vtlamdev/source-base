package com.vtlamdev.sourcebase.common.data.security;

public enum Authority {

    SYS_ADMIN(0),
    TENANT_ADMIN(1),
    CUSTOMER_USER(2),
    REFRESH_TOKEN(10);

    private final int code;

    Authority(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static Authority parse(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        for (Authority authority : values()) {
            if (authority.name().equalsIgnoreCase(value)) {
                return authority;
            }
        }
        return null;
    }

}
