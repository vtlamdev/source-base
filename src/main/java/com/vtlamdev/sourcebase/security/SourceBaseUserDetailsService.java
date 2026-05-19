package com.vtlamdev.sourcebase.security;

import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.security.UserCredentials;
import com.vtlamdev.sourcebase.dao.service.UserCredentialsService;
import com.vtlamdev.sourcebase.dao.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class SourceBaseUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final UserCredentialsService userCredentialsService;

    public SourceBaseUserDetailsService(UserService userService, UserCredentialsService userCredentialsService) {
        this.userService = userService;
        this.userCredentialsService = userCredentialsService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new BadCredentialsException("Username not provided");
        }
        User user = userService.findUserByEmail(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        UserCredentials userCredentials = userCredentialsService.findUserCredentialsByUserId(TenantId.SYS_TENANT_ID, user.getId())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        if (!StringUtils.hasText(userCredentials.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return new SecurityUserPrincipal(user, userCredentials.getPassword(), userCredentials.isEnabled());
    }

}
