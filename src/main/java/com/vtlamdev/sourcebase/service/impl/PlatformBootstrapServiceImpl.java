package com.vtlamdev.sourcebase.service.impl;

import com.vtlamdev.sourcebase.common.data.AdminSettings;
import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.security.Authority;
import com.vtlamdev.sourcebase.common.data.security.UserCredentials;
import com.vtlamdev.sourcebase.common.util.JacksonUtil;
import com.vtlamdev.sourcebase.dao.service.UserCredentialsService;
import com.vtlamdev.sourcebase.dao.service.UserService;
import com.vtlamdev.sourcebase.service.AdminSettingsApplicationService;
import com.vtlamdev.sourcebase.service.PlatformBootstrapService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PlatformBootstrapServiceImpl implements PlatformBootstrapService {

    private final AdminSettingsApplicationService adminSettingsApplicationService;
    private final UserService userService;
    private final UserCredentialsService userCredentialsService;
    private final PasswordEncoder passwordEncoder;

    public PlatformBootstrapServiceImpl(AdminSettingsApplicationService adminSettingsApplicationService,
                                        UserService userService,
                                        UserCredentialsService userCredentialsService,
                                        PasswordEncoder passwordEncoder) {
        this.adminSettingsApplicationService = adminSettingsApplicationService;
        this.userService = userService;
        this.userCredentialsService = userCredentialsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void initializeBaseSettings() {
        AdminSettings existing = adminSettingsApplicationService.getByKey(TenantId.SYS_TENANT_ID, "platform");
        if (existing == null) {
            AdminSettings settings = new AdminSettings();
            settings.setTenantId(TenantId.SYS_TENANT_ID);
            settings.setKey("platform");
            settings.setJsonValue(JacksonUtil.valueToTree(Map.of(
                    "baseSource", true,
                    "inspiredBy", "thingsboard"
            )));
            adminSettingsApplicationService.save(TenantId.SYS_TENANT_ID, settings);
        }

        if (userService.findUserByEmail(TenantId.SYS_TENANT_ID, "sysadmin@sourcebase.local").isEmpty()) {
            User admin = new User();
            admin.setTenantId(TenantId.SYS_TENANT_ID);
            admin.setEmail("sysadmin@sourcebase.local");
            admin.setAuthority(Authority.SYS_ADMIN);
            admin.setFirstName("System");
            admin.setLastName("Administrator");
            admin = userService.saveUser(TenantId.SYS_TENANT_ID, admin);

            UserCredentials credentials = new UserCredentials();
            credentials.setUserId(admin.getId());
            credentials.setEnabled(true);
            credentials.setPassword(passwordEncoder.encode("sysadmin"));
            credentials.setFailedLoginAttempts(0);
            userCredentialsService.saveUserCredentials(TenantId.SYS_TENANT_ID, credentials);
        }
    }

}
