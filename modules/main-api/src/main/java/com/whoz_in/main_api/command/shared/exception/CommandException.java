package com.whoz_in.main_api.command.shared.exception;

import com.whoz_in.main_api.shared.application.ExceptionResponsibility;

public class CommandException extends RuntimeException{

    private final String errorCode;

    public CommandException( String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
