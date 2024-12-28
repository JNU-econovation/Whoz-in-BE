package com.whoz_in.domain.shared;

import lombok.Getter;

//비즈니스 내에서 발생하는 예외이므로 HttpStatus는 담지 않습니다.
@Getter
public abstract class BusinessException extends RuntimeException{
    private final String errorCode;

    protected BusinessException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
