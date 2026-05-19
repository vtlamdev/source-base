package com.vtlamdev.sourcebase.dao.model;

import com.vtlamdev.sourcebase.common.data.BaseData;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public abstract class BaseSqlEntity<D> implements BaseEntity<D> {

    @Id
    @Column(name = ModelConstants.ID_PROPERTY, nullable = false, updatable = false)
    protected UUID id;

    @Column(name = ModelConstants.CREATED_TIME_PROPERTY, nullable = false)
    protected long createdTime;

    protected BaseSqlEntity() {
    }

    protected BaseSqlEntity(BaseData<?> domain) {
        this.id = domain.getUuidId();
        this.createdTime = domain.getCreatedTime();
    }

    protected BaseSqlEntity(BaseSqlEntity<?> entity) {
        this.id = entity.id;
        this.createdTime = entity.createdTime;
    }

    @Override
    public UUID getUuid() {
        return id;
    }

    @Override
    public void setUuid(UUID id) {
        this.id = id;
    }

    @Override
    public long getCreatedTime() {
        return createdTime;
    }

    @Override
    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

}
