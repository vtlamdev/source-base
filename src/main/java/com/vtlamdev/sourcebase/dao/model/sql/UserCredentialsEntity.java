package com.vtlamdev.sourcebase.dao.model.sql;

import com.vtlamdev.sourcebase.common.data.id.UserCredentialsId;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import com.vtlamdev.sourcebase.common.data.security.UserCredentials;
import com.vtlamdev.sourcebase.dao.DaoUtil;
import com.vtlamdev.sourcebase.dao.model.BaseSqlEntity;
import com.vtlamdev.sourcebase.dao.model.ModelConstants;
import com.vtlamdev.sourcebase.dao.util.mapping.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Entity
@Table(name = ModelConstants.USER_CREDENTIALS_TABLE_NAME)
public class UserCredentialsEntity extends BaseSqlEntity<UserCredentials> {

    @Column(name = ModelConstants.USER_CREDENTIALS_USER_ID_PROPERTY, unique = true)
    private UUID userId;

    @Column(name = ModelConstants.USER_CREDENTIALS_ENABLED_PROPERTY, nullable = false)
    private boolean enabled;

    @Column(name = ModelConstants.USER_CREDENTIALS_PASSWORD_PROPERTY, nullable = false)
    private String password;

    @Column(name = ModelConstants.USER_CREDENTIALS_ACTIVATE_TOKEN_PROPERTY, unique = true)
    private String activateToken;

    @Column(name = ModelConstants.USER_CREDENTIALS_ACTIVATE_TOKEN_EXP_TIME_PROPERTY)
    private Long activateTokenExpTime;

    @Column(name = ModelConstants.USER_CREDENTIALS_RESET_TOKEN_PROPERTY, unique = true)
    private String resetToken;

    @Column(name = ModelConstants.USER_CREDENTIALS_RESET_TOKEN_EXP_TIME_PROPERTY)
    private Long resetTokenExpTime;

    @Convert(converter = JsonConverter.class)
    @Column(name = ModelConstants.ADDITIONAL_INFO_PROPERTY, columnDefinition = "text")
    private JsonNode additionalInfo;

    @Column(name = ModelConstants.USER_CREDENTIALS_LAST_LOGIN_TS_PROPERTY)
    private Long lastLoginTs;

    @Column(name = ModelConstants.USER_CREDENTIALS_FAILED_LOGIN_ATTEMPTS_PROPERTY)
    private Integer failedLoginAttempts;

    public UserCredentialsEntity() {
    }

    public UserCredentialsEntity(UserCredentials userCredentials) {
        super(userCredentials);
        this.userId = DaoUtil.getId(userCredentials.getUserId());
        this.enabled = userCredentials.isEnabled();
        this.password = userCredentials.getPassword();
        this.activateToken = userCredentials.getActivateToken();
        this.activateTokenExpTime = userCredentials.getActivateTokenExpTime();
        this.resetToken = userCredentials.getResetToken();
        this.resetTokenExpTime = userCredentials.getResetTokenExpTime();
        this.additionalInfo = userCredentials.getAdditionalInfo();
        this.lastLoginTs = userCredentials.getLastLoginTs();
        this.failedLoginAttempts = userCredentials.getFailedLoginAttempts();
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public UserCredentials toData() {
        UserCredentials userCredentials = new UserCredentials(new UserCredentialsId(id));
        userCredentials.setCreatedTime(createdTime);
        userCredentials.setUserId(DaoUtil.toEntityId(userId, UserId::new));
        userCredentials.setEnabled(enabled);
        userCredentials.setPassword(password);
        userCredentials.setActivateToken(activateToken);
        userCredentials.setActivateTokenExpTime(activateTokenExpTime);
        userCredentials.setResetToken(resetToken);
        userCredentials.setResetTokenExpTime(resetTokenExpTime);
        userCredentials.setAdditionalInfo(additionalInfo);
        userCredentials.setLastLoginTs(lastLoginTs);
        userCredentials.setFailedLoginAttempts(failedLoginAttempts);
        return userCredentials;
    }

}
