package com.vtlamdev.sourcebase.common.data.id;

import java.util.UUID;

public abstract class IdBased<I extends UUIDBased> implements HasId<I> {

    protected I id;

    protected IdBased() {
    }

    protected IdBased(I id) {
        this.id = id;
    }

    public void setId(I id) {
        this.id = id;
    }

    @Override
    public I getId() {
        return id;
    }

    public UUID getUuidId() {
        return id != null ? id.getId() : null;
    }

}
