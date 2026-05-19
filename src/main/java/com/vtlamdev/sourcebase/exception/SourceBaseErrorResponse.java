package com.vtlamdev.sourcebase.exception;

import com.vtlamdev.sourcebase.common.data.exception.SourceBaseErrorCode;

import java.time.Instant;

public class SourceBaseErrorResponse {

    private final Instant timestamp = Instant.now();
    private final SourceBaseErrorCode errorCode;
    private final String message;
    private final String path;

    public SourceBaseErrorResponse(SourceBaseErrorCode errorCode, String message, String path) {
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public SourceBaseErrorCode getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

}
