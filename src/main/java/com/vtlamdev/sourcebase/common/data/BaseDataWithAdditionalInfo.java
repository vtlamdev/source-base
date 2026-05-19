package com.vtlamdev.sourcebase.common.data;

import com.vtlamdev.sourcebase.common.data.id.UUIDBased;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.ByteArrayInputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class BaseDataWithAdditionalInfo<I extends UUIDBased> extends BaseData<I> implements HasAdditionalInfo {

    private static final Logger log = LoggerFactory.getLogger(BaseDataWithAdditionalInfo.class);

    private transient JsonNode additionalInfo;
    private byte[] additionalInfoBytes;

    protected BaseDataWithAdditionalInfo() {
        super();
    }

    protected BaseDataWithAdditionalInfo(I id) {
        super(id);
    }

    protected BaseDataWithAdditionalInfo(BaseDataWithAdditionalInfo<I> data) {
        super(data);
        setAdditionalInfo(data.getAdditionalInfo());
    }

    @Override
    public JsonNode getAdditionalInfo() {
        return getJson(() -> additionalInfo, () -> additionalInfoBytes);
    }

    public void setAdditionalInfo(JsonNode additionalInfo) {
        setJson(additionalInfo, value -> this.additionalInfo = value, value -> this.additionalInfoBytes = value);
    }

    public void setAdditionalInfoField(String field, JsonNode value) {
        JsonNode root = getAdditionalInfo();
        if (!(root instanceof ObjectNode)) {
            ObjectNode objectNode = MAPPER.createObjectNode();
            root = objectNode;
        }
        ((ObjectNode) root).set(field, value);
        setAdditionalInfo(root);
    }

    public static JsonNode getJson(Supplier<JsonNode> jsonData, Supplier<byte[]> binaryData) {
        JsonNode json = jsonData.get();
        if (json != null) {
            return json;
        }
        byte[] data = binaryData.get();
        if (data == null) {
            return null;
        }
        try {
            return MAPPER.readTree(new ByteArrayInputStream(data));
        } catch (Exception e) {
            log.warn("Failed to deserialize json payload", e);
            return null;
        }
    }

    public static void setJson(JsonNode json, Consumer<JsonNode> jsonConsumer, Consumer<byte[]> bytesConsumer) {
        jsonConsumer.accept(json);
        try {
            bytesConsumer.accept(json == null ? null : MAPPER.writeValueAsBytes(json));
        } catch (Exception e) {
            log.warn("Failed to serialize json payload", e);
        }
    }

}
