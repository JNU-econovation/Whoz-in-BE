package com.whoz_in.main_api.query.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidPageParameterException extends WhozinException {
  public static final InvalidPageParameterException EXCEPTION = new InvalidPageParameterException();
    private InvalidPageParameterException() {
        super("6010", "잘못된 파라미터 : size");
    }
}
