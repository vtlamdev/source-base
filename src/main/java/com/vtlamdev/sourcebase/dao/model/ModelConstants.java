package com.vtlamdev.sourcebase.dao.model;

public final class ModelConstants {

    public static final String ID_PROPERTY = "id";
    public static final String CREATED_TIME_PROPERTY = "created_time";
    public static final String VERSION_PROPERTY = "version";
    public static final String DELETED_PROPERTY = "deleted";
    public static final String DELETED_TIME_PROPERTY = "deleted_time";

    public static final String TENANT_ID_PROPERTY = "tenant_id";
    public static final String CUSTOMER_ID_PROPERTY = "customer_id";
    public static final String USER_ID_PROPERTY = "user_id";
    public static final String PHONE_PROPERTY = "phone";
    public static final String ADDRESS_PROPERTY = "address";
    public static final String ADDITIONAL_INFO_PROPERTY = "additional_info";

    public static final String TENANT_TABLE_NAME = "tenant";
    public static final String TENANT_TITLE_PROPERTY = "title";
    public static final String TENANT_REGION_PROPERTY = "region";
    public static final String TENANT_COUNTRY_PROPERTY = "country";
    public static final String TENANT_STATE_PROPERTY = "state";
    public static final String TENANT_EMAIL_PROPERTY = "email";

    public static final String USER_TABLE_NAME = "tb_user";
    public static final String USER_EMAIL_PROPERTY = "email";
    public static final String USER_AUTHORITY_PROPERTY = "authority";
    public static final String USER_FIRST_NAME_PROPERTY = "first_name";
    public static final String USER_LAST_NAME_PROPERTY = "last_name";
    public static final String USER_CREDENTIALS_TABLE_NAME = "user_credentials";
    public static final String USER_CREDENTIALS_USER_ID_PROPERTY = USER_ID_PROPERTY;
    public static final String USER_CREDENTIALS_ENABLED_PROPERTY = "enabled";
    public static final String USER_CREDENTIALS_PASSWORD_PROPERTY = "password";
    public static final String USER_CREDENTIALS_ACTIVATE_TOKEN_PROPERTY = "activate_token";
    public static final String USER_CREDENTIALS_ACTIVATE_TOKEN_EXP_TIME_PROPERTY = "activate_token_exp_time";
    public static final String USER_CREDENTIALS_RESET_TOKEN_PROPERTY = "reset_token";
    public static final String USER_CREDENTIALS_RESET_TOKEN_EXP_TIME_PROPERTY = "reset_token_exp_time";
    public static final String USER_CREDENTIALS_LAST_LOGIN_TS_PROPERTY = "last_login_ts";
    public static final String USER_CREDENTIALS_FAILED_LOGIN_ATTEMPTS_PROPERTY = "failed_login_attempts";

    public static final String ADMIN_SETTINGS_TABLE_NAME = "admin_settings";
    public static final String ADMIN_SETTINGS_KEY_PROPERTY = "settings_key";
    public static final String ADMIN_SETTINGS_JSON_VALUE_PROPERTY = "json_value";

    private ModelConstants() {
    }

}
