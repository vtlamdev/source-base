package com.vtlamdev.sourcebase.dao.model.sql;

import com.vtlamdev.sourcebase.common.data.User;
import com.vtlamdev.sourcebase.common.data.id.CustomerId;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
import com.vtlamdev.sourcebase.common.data.id.UserId;
import com.vtlamdev.sourcebase.common.data.security.Authority;
import com.vtlamdev.sourcebase.dao.DaoUtil;
import com.vtlamdev.sourcebase.dao.model.BaseVersionedEntity;
import com.vtlamdev.sourcebase.dao.model.ModelConstants;
import com.vtlamdev.sourcebase.dao.util.mapping.JsonConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import tools.jackson.databind.JsonNode;

import java.util.UUID;

@Entity
@Table(name = ModelConstants.USER_TABLE_NAME)
public class UserEntity extends BaseVersionedEntity<User> {

    @Column(name = ModelConstants.TENANT_ID_PROPERTY)
    private UUID tenantId;

    @Column(name = ModelConstants.CUSTOMER_ID_PROPERTY)
    private UUID customerId;

    @Enumerated(EnumType.STRING)
    @Column(name = ModelConstants.USER_AUTHORITY_PROPERTY, nullable = false)
    private Authority authority;

    @Column(name = ModelConstants.USER_EMAIL_PROPERTY, nullable = false, unique = true)
    private String email;

    @Column(name = ModelConstants.USER_FIRST_NAME_PROPERTY)
    private String firstName;

    @Column(name = ModelConstants.USER_LAST_NAME_PROPERTY)
    private String lastName;

    @Column(name = ModelConstants.PHONE_PROPERTY)
    private String phone;

    @Convert(converter = JsonConverter.class)
    @Column(name = ModelConstants.ADDITIONAL_INFO_PROPERTY, columnDefinition = "text")
    private JsonNode additionalInfo;

    public UserEntity() {
    }

    public UserEntity(User user) {
        super(user);
        this.tenantId = DaoUtil.getId(user.getTenantId());
        this.customerId = DaoUtil.getId(user.getCustomerId());
        this.authority = user.getAuthority();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
        this.additionalInfo = user.getAdditionalInfo();
    }

    @Override
    public User toData() {
        User user = new User(new UserId(id));
        user.setCreatedTime(createdTime);
        user.setVersion(version);
        user.setAuthority(authority);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setAdditionalInfo(additionalInfo);
        user.setTenantId(DaoUtil.toEntityId(tenantId, TenantId::fromUUID));
        user.setCustomerId(DaoUtil.toEntityId(customerId, CustomerId::new));
        return user;
    }

}
