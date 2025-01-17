package com.whoz_in.main_api.shared.application;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException{
    private final String errorCode;

    protected ApplicationException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
