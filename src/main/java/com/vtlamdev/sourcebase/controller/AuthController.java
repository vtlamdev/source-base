package com.vtlamdev.sourcebase.controller;

import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.security.model.JwtPair;
import com.vtlamdev.sourcebase.controller.base.BaseController;
import com.vtlamdev.sourcebase.security.SecurityContextUtils;
import com.vtlamdev.sourcebase.service.security.SourceBaseAuthenticationService;
import com.vtlamdev.sourcebase.service.security.auth.jwt.RefreshTokenRequest;
import com.vtlamdev.sourcebase.service.security.auth.rest.ChangePasswordRequest;
import com.vtlamdev.sourcebase.service.security.auth.rest.LoginRequest;
import com.vtlamdev.sourcebase.service.security.auth.rest.LoginResponse;
import com.vtlamdev.sourcebase.service.security.auth.rest.RegisterRequest;
import com.vtlamdev.sourcebase.service.security.model.SecurityUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth")
public class AuthController extends BaseController {

    private final SourceBaseAuthenticationService authenticationService;

    public AuthController(SourceBaseAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        JwtPair jwtPair = authenticationService.login(request.getUsername(), request.getPassword());
        return new LoginResponse(jwtPair.getToken(), jwtPair.getRefreshToken());
    }

    @PostMapping("/register")
    public LoginResponse register(@RequestBody RegisterRequest request) {
        JwtPair jwtPair = authenticationService.register(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhone()
        );
        return new LoginResponse(jwtPair.getToken(), jwtPair.getRefreshToken());
    }

    @PostMapping("/token")
    public LoginResponse refresh(@RequestBody RefreshTokenRequest request) {
        JwtPair jwtPair = authenticationService.refreshToken(request.refreshToken());
        return new LoginResponse(jwtPair.getToken(), jwtPair.getRefreshToken());
    }

    @GetMapping("/user")
    public User currentUser() {
        SecurityUser currentUser = requireCurrentUser();
        return authenticationService.currentUser(currentUser);
    }

    @PostMapping("/changePassword")
    public LoginResponse changePassword(@RequestBody ChangePasswordRequest request) {
        SecurityUser currentUser = requireCurrentUser();
        JwtPair jwtPair = authenticationService.changePassword(currentUser, request.getCurrentPassword(), request.getNewPassword());
        return new LoginResponse(jwtPair.getToken(), jwtPair.getRefreshToken());
    }

    private SecurityUser requireCurrentUser() {
        SecurityUser currentUser = SecurityContextUtils.currentUser();
        if (currentUser == null) {
            throw new BadCredentialsException("No authenticated user");
        }
        return currentUser;
    }

}
