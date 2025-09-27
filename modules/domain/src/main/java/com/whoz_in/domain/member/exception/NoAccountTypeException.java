package com.whoz_in.domain.member.exception;

import com.whoz_in.shared.WhozinException;

public class NoAccountTypeException extends WhozinException {
    public static final NoAccountTypeException EXCEPTION = new NoAccountTypeException();
    private NoAccountTypeException() {
        super("2030", "no account type");
    }
}
