package com.vtlamdev.sourcebase.service.security;

import com.vtlamdev.sourcebase.cache.UserProfileCache;
import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.security.UserCredentials;
import com.vtlamdev.sourcebase.common.data.security.Authority;
import com.vtlamdev.sourcebase.common.data.security.model.JwtPair;
import com.vtlamdev.sourcebase.dao.service.UserCredentialsService;
import com.vtlamdev.sourcebase.dao.service.UserService;
import com.vtlamdev.sourcebase.exception.SourceBaseException;
import com.vtlamdev.sourcebase.exception.ResourceNotFoundException;
import com.vtlamdev.sourcebase.security.SecurityUserPrincipal;
import com.vtlamdev.sourcebase.common.data.exception.SourceBaseErrorCode;
import com.vtlamdev.sourcebase.service.security.model.SecurityUser;
import com.vtlamdev.sourcebase.service.security.model.UserPrincipal;
import com.vtlamdev.sourcebase.service.security.model.token.JwtTokenFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Locale;
import java.util.Optional;

@Service
public class SourceBaseAuthenticationService {

    private final UserService userService;
    private final UserCredentialsService userCredentialsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenFactory jwtTokenFactory;
    private final AuthenticationManager authenticationManager;
    private final UserProfileCache userProfileCache;

    public SourceBaseAuthenticationService(UserService userService,
                                           UserCredentialsService userCredentialsService,
                                           PasswordEncoder passwordEncoder,
                                           JwtTokenFactory jwtTokenFactory,
                                           AuthenticationManager authenticationManager,
                                           UserProfileCache userProfileCache) {
        this.userService = userService;
        this.userCredentialsService = userCredentialsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenFactory = jwtTokenFactory;
        this.authenticationManager = authenticationManager;
        this.userProfileCache = userProfileCache;
    }

    public JwtPair login(String username, String password) {
        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) {
            throw new BadCredentialsException("Username or password not provided");
        }
        Optional<User> existingUser = userService.findUserByEmail(username);
        Optional<UserCredentials> existingCredentials = existingUser
                .flatMap(user -> userCredentialsService.findUserCredentialsByUserId(TenantId.SYS_TENANT_ID, user.getId()));
        try {
            SecurityUserPrincipal principal = (SecurityUserPrincipal) authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(username, password)
            ).getPrincipal();
            User user = principal.getUser();
            userCredentialsService.setLastLoginTs(TenantId.SYS_TENANT_ID, user.getId(), System.currentTimeMillis());
            userCredentialsService.setFailedLoginAttempts(TenantId.SYS_TENANT_ID, user.getId(), 0);
            return jwtTokenFactory.createTokenPair(new SecurityUser(user, principal.isEnabled(), new UserPrincipal(UserPrincipal.Type.USER_NAME, user.getEmail())));
        } catch (BadCredentialsException e) {
            if (existingUser.isPresent() && existingCredentials.isPresent()) {
                int failedAttempts = existingCredentials.get().getFailedLoginAttempts() != null ? existingCredentials.get().getFailedLoginAttempts() : 0;
                userCredentialsService.setFailedLoginAttempts(TenantId.SYS_TENANT_ID, existingUser.get().getId(), failedAttempts + 1);
            }
            throw e;
        }
    }

    public JwtPair refreshToken(String refreshToken) {
        SecurityUser refreshUser = jwtTokenFactory.parseRefreshToken(refreshToken);
        User user = userService.findUserById(TenantId.SYS_TENANT_ID, refreshUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for refresh token"));
        UserCredentials userCredentials = userCredentialsService.findUserCredentialsByUserId(TenantId.SYS_TENANT_ID, user.getId())
                .orElseThrow(() -> new BadCredentialsException("User credentials are disabled"));
        if (!userCredentials.isEnabled()) {
            throw new BadCredentialsException("User credentials are disabled");
        }
        SecurityUser securityUser = new SecurityUser(user, true, new UserPrincipal(UserPrincipal.Type.USER_NAME, user.getEmail()));
        securityUser.setSessionId(refreshUser.getSessionId());
        return jwtTokenFactory.createTokenPair(securityUser);
    }

    public User currentUser(SecurityUser securityUser) {
        User user = userProfileCache.getOrFetch(securityUser.getId(), () -> userService.findUserById(TenantId.SYS_TENANT_ID, securityUser.getId())
                .orElse(null));
        if (user == null) {
            throw new ResourceNotFoundException("Current user not found");
        }
        return user;
    }

    public JwtPair register(String email, String password, String firstName, String lastName, String phone) {
        String normalizedEmail = normalizeEmail(email);
        if (!StringUtils.hasText(normalizedEmail) || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException("Email or password not provided");
        }
        if (userService.findUserByEmail(normalizedEmail).isPresent()) {
            throw new SourceBaseException(SourceBaseErrorCode.VALIDATION, "Email is already registered");
        }

        User user = new User();
        user.setTenantId(TenantId.SYS_TENANT_ID);
        user.setEmail(normalizedEmail);
        user.setAuthority(Authority.CUSTOMER_USER);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user = userService.saveUser(TenantId.SYS_TENANT_ID, user);

        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUserId(user.getId());
        userCredentials.setEnabled(true);
        userCredentials.setPassword(passwordEncoder.encode(password));
        userCredentials.setFailedLoginAttempts(0);
        userCredentials.setLastLoginTs(System.currentTimeMillis());
        userCredentialsService.saveUserCredentials(TenantId.SYS_TENANT_ID, userCredentials);

        return jwtTokenFactory.createTokenPair(new SecurityUser(user, true, new UserPrincipal(UserPrincipal.Type.USER_NAME, user.getEmail())));
    }

    public JwtPair changePassword(SecurityUser securityUser, String currentPassword, String newPassword) {
        UserCredentials userCredentials = userCredentialsService.findUserCredentialsByUserId(TenantId.SYS_TENANT_ID, securityUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User credentials not found"));
        if (!passwordEncoder.matches(currentPassword, userCredentials.getPassword())) {
            throw new BadCredentialsException("Current password doesn't match");
        }
        if (!StringUtils.hasText(newPassword) || passwordEncoder.matches(newPassword, userCredentials.getPassword())) {
            throw new BadCredentialsException("New password should be different from existing");
        }
        userCredentials.setPassword(passwordEncoder.encode(newPassword));
        userCredentialsService.replaceUserCredentials(TenantId.SYS_TENANT_ID, userCredentials);
        User user = userService.findUserById(TenantId.SYS_TENANT_ID, securityUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        return jwtTokenFactory.createTokenPair(new SecurityUser(user, true, new UserPrincipal(UserPrincipal.Type.USER_NAME, user.getEmail())));
    }

    private String normalizeEmail(String email) {
        if (!StringUtils.hasText(email)) {
            return email;
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

}
