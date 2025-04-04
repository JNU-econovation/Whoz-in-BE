package com.whoz_in.domain.member.exception;

import com.whoz_in.shared.WhozinException;

public class WrongPasswordException extends WhozinException {
    public static final WrongPasswordException EXCEPTION = new WrongPasswordException();
    private WrongPasswordException() {
        super("2021", "틀린 비밀번호입니다.");
    }
}
