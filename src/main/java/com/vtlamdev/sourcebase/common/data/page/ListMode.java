package com.vtlamdev.sourcebase.common.data.page;

public enum ListMode {
    PAGE,
    SCROLL;

    public static ListMode fromValue(String value) {
        if (value == null || value.isBlank()) {
            return PAGE;
        }
        return ListMode.valueOf(value.trim().toUpperCase());
    }
}
