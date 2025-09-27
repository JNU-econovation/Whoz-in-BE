package com.whoz_in.main_api.command.member.exception;

import com.whoz_in.shared.WhozinException;

public class AlreadySocialMemberException extends WhozinException {
    public static final AlreadySocialMemberException EXCEPTION = new AlreadySocialMemberException();
    private AlreadySocialMemberException() {
        super("2027", "이미 소셜 가입된 사용자입니다.");
    }
}
