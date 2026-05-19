package com.vtlamdev.sourcebase.common.data.security;

import com.vtlamdev.sourcebase.common.data.BaseDataWithAdditionalInfo;
import com.vtlamdev.sourcebase.common.data.id.UserCredentialsId;
import com.vtlamdev.sourcebase.common.data.id.UserId;

public class UserCredentials extends BaseDataWithAdditionalInfo<UserCredentialsId> {

    private UserId userId;
    private boolean enabled;
    private String password;
    private String activateToken;
    private Long activateTokenExpTime;
    private String resetToken;
    private Long resetTokenExpTime;
    private Long lastLoginTs;
    private Integer failedLoginAttempts;

    public UserCredentials() {
    }

    public UserCredentials(UserCredentialsId id) {
        super(id);
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(UserId userId) {
        this.userId = userId;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActivateToken() {
        return activateToken;
    }

    public void setActivateToken(String activateToken) {
        this.activateToken = activateToken;
    }

    public Long getActivateTokenExpTime() {
        return activateTokenExpTime;
    }

    public void setActivateTokenExpTime(Long activateTokenExpTime) {
        this.activateTokenExpTime = activateTokenExpTime;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public Long getResetTokenExpTime() {
        return resetTokenExpTime;
    }

    public void setResetTokenExpTime(Long resetTokenExpTime) {
        this.resetTokenExpTime = resetTokenExpTime;
    }

    public Long getLastLoginTs() {
        return lastLoginTs;
    }

    public void setLastLoginTs(Long lastLoginTs) {
        this.lastLoginTs = lastLoginTs;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public boolean isActivationTokenExpired() {
        return getActivationTokenTtl() == 0;
    }

    public long getActivationTokenTtl() {
        return activateTokenExpTime != null ? Math.max(activateTokenExpTime - System.currentTimeMillis(), 0) : 0;
    }

    public boolean isResetTokenExpired() {
        return getResetTokenTtl() == 0;
    }

    public long getResetTokenTtl() {
        return resetTokenExpTime != null ? Math.max(resetTokenExpTime - System.currentTimeMillis(), 0) : 0;
    }

}
