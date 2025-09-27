package com.whoz_in.main_api.query.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidSizeParameterException extends WhozinException {
    public static final InvalidSizeParameterException EXCEPTION = new InvalidSizeParameterException();
    private InvalidSizeParameterException() {
        super("6009", "잘못된 파라미터 : size");
    }
}
