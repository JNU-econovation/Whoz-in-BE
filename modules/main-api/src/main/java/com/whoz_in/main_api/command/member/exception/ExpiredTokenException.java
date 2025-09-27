package com.whoz_in.main_api.command.member.exception;

import com.whoz_in.shared.WhozinException;

public class ExpiredTokenException extends WhozinException {
    public static final ExpiredTokenException EXCEPTION = new ExpiredTokenException();
    private ExpiredTokenException() {
        super("2029","만료된 토큰");
    }
}
