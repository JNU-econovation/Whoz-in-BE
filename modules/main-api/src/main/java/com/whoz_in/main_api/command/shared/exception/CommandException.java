package com.whoz_in.main_api.command.shared.exception;

import com.whoz_in.main_api.shared.application.ExceptionResponsibility;

public class CommandException extends RuntimeException{

    private final ExceptionResponsibility responsibility;
    private final String errorCode;

    public CommandException(ExceptionResponsibility responsibility, String errorCode, String message) {
        super(message);
        this.responsibility = responsibility;
        this.errorCode = errorCode;
    }

}
