package com.vtlamdev.sourcebase.common.data;

import com.vtlamdev.sourcebase.common.data.id.TenantId;

public class Tenant extends BaseDataWithAdditionalInfo<TenantId> implements HasName, HasVersion {

    private String title;
    private String region;
    private String country;
    private TenantState state;
    private String email;
    private String phone;
    private String address;
    private Long version;

    public Tenant() {
    }

    public Tenant(TenantId id) {
        super(id);
    }

    public Tenant(Tenant tenant) {
        super(tenant);
        this.title = tenant.getTitle();
        this.region = tenant.getRegion();
        this.country = tenant.getCountry();
        this.state = tenant.getState();
        this.email = tenant.getEmail();
        this.phone = tenant.getPhone();
        this.address = tenant.getAddress();
        this.version = tenant.getVersion();
    }

    @Override
    public String getName() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public TenantState getState() {
        return state;
    }

    public void setState(TenantState state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public void setVersion(Long version) {
        this.version = version;
    }

}
