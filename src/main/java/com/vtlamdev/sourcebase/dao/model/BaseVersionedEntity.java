package com.vtlamdev.sourcebase.dao.model;

import com.vtlamdev.sourcebase.common.data.HasVersion;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@MappedSuperclass
public abstract class BaseVersionedEntity<D> extends BaseSqlEntity<D> implements HasVersion {

    @Version
    @Column(name = ModelConstants.VERSION_PROPERTY)
    protected Long version;

    protected BaseVersionedEntity() {
    }

    protected BaseVersionedEntity(D domain) {
        super((com.vtlamdev.sourcebase.common.data.BaseData<?>) domain);
        if (domain instanceof HasVersion hasVersion) {
            this.version = hasVersion.getVersion();
        }
    }

    protected BaseVersionedEntity(BaseVersionedEntity<?> entity) {
        super(entity);
        this.version = entity.version;
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
