package com.whoz_in.domain.member.exception;

import com.whoz_in.shared.WhozinException;

public class NoPositionException extends WhozinException {
    public static final NoPositionException EXCEPTION = new NoPositionException();
    private NoPositionException() {
        super("2026", "존재하지 않는 분야입니다.");
    }
}
