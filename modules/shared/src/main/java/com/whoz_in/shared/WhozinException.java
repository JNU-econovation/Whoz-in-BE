package com.whoz_in.shared;

import lombok.Getter;

// 어디서든 사용할 수 있는 예외입니다. 따라서 HttpStatus는 담지 않습니다.
@Getter
public abstract class WhozinException extends RuntimeException{
    private final String errorCode;

    protected WhozinException(String errorCode, String errorMessage) {
        super(errorMessage);
        this.errorCode = errorCode;
    }
}
