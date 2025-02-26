package com.whoz_in.network_api.common.validation;


//검증 원인을 여러 개 담고 싶었는데, Spring Validator는 단일 객체를 대상으로 하는 필드 중심 검증이기 때문에 만듦
@FunctionalInterface
public interface Validator<T> {
    ValidationResult getValidationResult(T target);

    default void validate(T target){
        ValidationResult validationResult = getValidationResult(target);
        if (validationResult.hasError()) {
            throw new ValidationException(validationResult);
        }
    }
}
