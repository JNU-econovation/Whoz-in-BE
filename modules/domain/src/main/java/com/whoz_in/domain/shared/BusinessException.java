package com.whoz_in.domain.shared;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

//비즈니스 내에서 발생하는 예외이므로 HttpStatus는 담지 않습니다.
@Getter
@RequiredArgsConstructor
public abstract class BusinessException extends RuntimeException{
    private final String errorCode;
    private final String errorMessage;
}
