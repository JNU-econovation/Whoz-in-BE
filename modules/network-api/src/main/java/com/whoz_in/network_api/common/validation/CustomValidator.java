package com.whoz_in.network_api.common.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class CustomValidator<T extends ValidationRequest> implements Validator {

    @Override
    public final boolean supports(Class<?> clazz) {
        return ValidationRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public final void validate(Object target, Errors errors) {
        if (!(target instanceof ValidationRequest)) {
            throw new IllegalArgumentException("검증 대상은 ValidationRequest 타입이어야 합니다.");
        }
        validate((T) target, errors);
    }

    // T를 인자로 하는 추상 메서드 정의
    public abstract void validate(T target, Errors errors);
}
