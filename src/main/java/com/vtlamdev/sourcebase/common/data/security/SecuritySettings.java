package com.vtlamdev.sourcebase.common.data.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Schema
public class SecuritySettings implements Serializable {

    private UserPasswordPolicy passwordPolicy;
    private Integer maxFailedLoginAttempts;
    private String userLockoutNotificationEmail;
    private Integer mobileSecretKeyLength;

    @NotNull
    @Min(1)
    @Max(24)
    private Integer userActivationTokenTtl = 24;

    @NotNull
    @Min(1)
    @Max(24)
    private Integer passwordResetTokenTtl = 24;

    public UserPasswordPolicy getPasswordPolicy() {
        return passwordPolicy;
    }

    public void setPasswordPolicy(UserPasswordPolicy passwordPolicy) {
        this.passwordPolicy = passwordPolicy;
    }

    public Integer getMaxFailedLoginAttempts() {
        return maxFailedLoginAttempts;
    }

    public void setMaxFailedLoginAttempts(Integer maxFailedLoginAttempts) {
        this.maxFailedLoginAttempts = maxFailedLoginAttempts;
    }

    public String getUserLockoutNotificationEmail() {
        return userLockoutNotificationEmail;
    }

    public void setUserLockoutNotificationEmail(String userLockoutNotificationEmail) {
        this.userLockoutNotificationEmail = userLockoutNotificationEmail;
    }

    public Integer getMobileSecretKeyLength() {
        return mobileSecretKeyLength;
    }

    public void setMobileSecretKeyLength(Integer mobileSecretKeyLength) {
        this.mobileSecretKeyLength = mobileSecretKeyLength;
    }

    public Integer getUserActivationTokenTtl() {
        return userActivationTokenTtl;
    }

    public void setUserActivationTokenTtl(Integer userActivationTokenTtl) {
        this.userActivationTokenTtl = userActivationTokenTtl;
    }

    public Integer getPasswordResetTokenTtl() {
        return passwordResetTokenTtl;
    }

    public void setPasswordResetTokenTtl(Integer passwordResetTokenTtl) {
        this.passwordResetTokenTtl = passwordResetTokenTtl;
    }

}
