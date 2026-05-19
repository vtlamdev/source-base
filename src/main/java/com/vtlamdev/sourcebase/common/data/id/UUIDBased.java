package com.vtlamdev.sourcebase.common.data.id;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.UUID;

@Schema
public abstract class UUIDBased implements HasUUID, Serializable {

    private transient int hash;
    private final UUID id;

    protected UUIDBased() {
        this(UUID.randomUUID());
    }

    protected UUIDBased(UUID id) {
        this.id = id;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public int hashCode() {
        if (hash == 0) {
            hash = id != null ? id.hashCode() : 0;
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        UUIDBased other = (UUIDBased) obj;
        return id != null ? id.equals(other.id) : other.id == null;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

}
