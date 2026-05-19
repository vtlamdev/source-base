package com.vtlamdev.sourcebase.exception;

import com.vtlamdev.sourcebase.common.data.exception.SourceBaseErrorCode;

public class ResourceNotFoundException extends SourceBaseException {

    public ResourceNotFoundException(String message) {
        super(SourceBaseErrorCode.NOT_FOUND, message);
    }

}
