package com.vtlamdev.sourcebase.security;

import com.vtlamdev.sourcebase.common.data.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class SecurityUserPrincipal implements UserDetails {

    private final User user;
    private final String password;
    private final boolean enabled;

    public SecurityUserPrincipal(User user, String password, boolean enabled) {
        this.user = user;
        this.password = password;
        this.enabled = enabled;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String authority = user.getAuthority() != null ? user.getAuthority().name() : "SYS_ADMIN";
        return List.of(new SimpleGrantedAuthority(authority));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
