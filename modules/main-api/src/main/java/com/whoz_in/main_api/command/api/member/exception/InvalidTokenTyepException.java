package com.whoz_in.main_api.command.api.member.exception;

import com.whoz_in.shared.WhozinException;

public class InvalidTokenTyepException extends WhozinException {
  public static final InvalidTokenTyepException EXCEPTION = new InvalidTokenTyepException();
    public InvalidTokenTyepException() {
        super("2030","맞지 않은 토큰 타입");
    }
}
