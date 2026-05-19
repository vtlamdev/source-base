package com.vtlamdev.sourcebase.dao.model.sql;

import com.vtlamdev.sourcebase.common.data.Tenant;
import com.vtlamdev.sourcebase.common.data.TenantState;
import com.vtlamdev.sourcebase.common.data.id.TenantId;
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

@Entity
@Table(name = ModelConstants.TENANT_TABLE_NAME)
public class TenantEntity extends BaseVersionedEntity<Tenant> {

    @Column(name = ModelConstants.TENANT_TITLE_PROPERTY, nullable = false)
    private String title;

    @Column(name = ModelConstants.TENANT_REGION_PROPERTY)
    private String region;

    @Column(name = ModelConstants.TENANT_COUNTRY_PROPERTY)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = ModelConstants.TENANT_STATE_PROPERTY, nullable = false)
    private TenantState state;

    @Column(name = ModelConstants.TENANT_EMAIL_PROPERTY)
    private String email;

    @Column(name = ModelConstants.PHONE_PROPERTY)
    private String phone;

    @Column(name = ModelConstants.ADDRESS_PROPERTY)
    private String address;

    @Convert(converter = JsonConverter.class)
    @Column(name = ModelConstants.ADDITIONAL_INFO_PROPERTY, columnDefinition = "text")
    private JsonNode additionalInfo;

    public TenantEntity() {
    }

    public TenantEntity(Tenant tenant) {
        super(tenant);
        this.title = tenant.getTitle();
        this.region = tenant.getRegion();
        this.country = tenant.getCountry();
        this.state = tenant.getState();
        this.email = tenant.getEmail();
        this.phone = tenant.getPhone();
        this.address = tenant.getAddress();
        this.additionalInfo = tenant.getAdditionalInfo();
    }

    @Override
    public Tenant toData() {
        Tenant tenant = new Tenant(new TenantId(id));
        tenant.setCreatedTime(createdTime);
        tenant.setVersion(version);
        tenant.setTitle(title);
        tenant.setRegion(region);
        tenant.setCountry(country);
        tenant.setState(state);
        tenant.setEmail(email);
        tenant.setPhone(phone);
        tenant.setAddress(address);
        tenant.setAdditionalInfo(additionalInfo);
        return tenant;
    }

}
