package com.vtlamdev.sourcebase.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class SoftDeletableEntity<D> extends BaseSqlEntity<D> {

    @Column(name = ModelConstants.DELETED_PROPERTY, nullable = false)
    protected boolean deleted;

    @Column(name = ModelConstants.DELETED_TIME_PROPERTY)
    protected Long deletedTime;

    protected SoftDeletableEntity() {
    }

    protected SoftDeletableEntity(com.vtlamdev.sourcebase.common.data.BaseData<?> domain) {
        super(domain);
    }

    protected SoftDeletableEntity(SoftDeletableEntity<?> entity) {
        super(entity);
        this.deleted = entity.deleted;
        this.deletedTime = entity.deletedTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Long deletedTime) {
        this.deletedTime = deletedTime;
    }

}
