package com.vtlamdev.sourcebase.exception;

import com.vtlamdev.sourcebase.common.data.exception.SourceBaseErrorCode;

public class SourceBaseException extends RuntimeException {

    private final SourceBaseErrorCode errorCode;

    public SourceBaseException(SourceBaseErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public SourceBaseErrorCode getErrorCode() {
        return errorCode;
    }

}
