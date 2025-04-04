package com.whoz_in.domain.member.exception;

import com.whoz_in.shared.WhozinException;

public class NoMemberException extends WhozinException {
    public static final NoMemberException EXCEPTION = new NoMemberException();
    private NoMemberException() {
        super("2024", "존재하지 않는 멤버입니다.");
    }
}
