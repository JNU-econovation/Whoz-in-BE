package com.whoz_in.network_api.common.validation;

@FunctionalInterface
public interface BiValidator<T, U>{
    ValidationResult getValidationResult(T target1, U target2);

    default void validate(T target1, U target2){
        ValidationResult validationResult = getValidationResult(target1, target2);
        if (validationResult.hasError()) {
            throw new ValidationException(validationResult);
        }
    }
}
