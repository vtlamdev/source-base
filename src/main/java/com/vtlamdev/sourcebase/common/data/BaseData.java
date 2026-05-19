package com.vtlamdev.sourcebase.common.data;

import com.vtlamdev.sourcebase.common.data.id.IdBased;
import com.vtlamdev.sourcebase.common.data.id.UUIDBased;
import io.swagger.v3.oas.annotations.media.Schema;
import tools.jackson.databind.ObjectMapper;

import java.io.Serializable;

public abstract class BaseData<I extends UUIDBased> extends IdBased<I> implements Serializable {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    protected long createdTime;

    protected BaseData() {
        super();
    }

    protected BaseData(I id) {
        super(id);
    }

    protected BaseData(BaseData<I> data) {
        super(data.getId());
        this.createdTime = data.getCreatedTime();
    }

    @Schema(description = "Entity creation timestamp in milliseconds since Unix epoch", accessMode = Schema.AccessMode.READ_ONLY)
    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

}
