package com.whoz_in.log_writer.common.validation;

@FunctionalInterface
public interface BiValidator<T, U>{
    ValidationResult getValidationResult(T t, U u);

    default void validate(T target1, U target2){
        ValidationResult validationResult = getValidationResult(target1, target2);
        if (validationResult.hasErrors()) {
            throw new ValidationException(validationResult);
        }
    }
}
