package com.whoz_in.network_api.common.validation;

public class ValidationException extends RuntimeException{

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(ValidationResult result) {
        super(String.join(", ", result.getErrors()));
    }
}
