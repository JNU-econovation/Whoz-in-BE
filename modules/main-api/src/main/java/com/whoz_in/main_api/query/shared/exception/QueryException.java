package com.whoz_in.main_api.query.shared.exception;

import com.whoz_in.main_api.shared.application.ExceptionResponsibility;

public class QueryException extends RuntimeException{

    private final ExceptionResponsibility responsibility;
    private final String errorCode;

    public QueryException(ExceptionResponsibility responsibility, String errorCode, String errorMessage) {
        super(errorMessage);
        this.responsibility = responsibility;
        this.errorCode = errorCode;
    }

}