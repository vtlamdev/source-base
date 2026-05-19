package com.vtlamdev.sourcebase.common.util;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

public final class JacksonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JacksonUtil() {
    }

    public static JsonNode toJsonNode(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return OBJECT_MAPPER.readTree(value);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to parse json payload", e);
        }
    }

    public static String toString(JsonNode node) {
        if (node == null) {
            return null;
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(node);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to serialize json payload", e);
        }
    }

    public static JsonNode valueToTree(Object value) {
        return OBJECT_MAPPER.valueToTree(value);
    }

    public static <T> T convertValue(JsonNode node, Class<T> type) {
        return OBJECT_MAPPER.convertValue(node, type);
    }

}
