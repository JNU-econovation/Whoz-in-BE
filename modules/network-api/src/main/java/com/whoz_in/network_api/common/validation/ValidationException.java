package com.whoz_in.network_api.common.validation;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException{
    private final ValidationResult validationResult;

    public ValidationException(ValidationResult result) {
        super(String.join(", ", result.getErrors()));
        this.validationResult = result;
    }
}
