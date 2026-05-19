package com.vtlamdev.sourcebase.dao.util.mapping;

import com.vtlamdev.sourcebase.common.util.JacksonUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import tools.jackson.databind.JsonNode;

@Converter
public class JsonConverter implements AttributeConverter<JsonNode, String> {

    @Override
    public String convertToDatabaseColumn(JsonNode attribute) {
        return JacksonUtil.toString(attribute);
    }

    @Override
    public JsonNode convertToEntityAttribute(String dbData) {
        return JacksonUtil.toJsonNode(dbData);
    }

}
