package com.whoz_in.domain.badge.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidFormColorCodeException extends WhozinException {
    public static final InvalidFormColorCodeException EXCEPTION = new InvalidFormColorCodeException();
    public InvalidFormColorCodeException() {super("5003","뱃지의 색상코드 형식이 올바르지 않습니다.");}
}
