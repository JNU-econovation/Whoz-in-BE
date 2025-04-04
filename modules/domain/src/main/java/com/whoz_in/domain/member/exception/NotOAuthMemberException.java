package com.whoz_in.domain.member.exception;

import com.whoz_in.shared.WhozinException;

public class NotOAuthMemberException extends WhozinException {
    public static final NotOAuthMemberException EXCEPTION = new NotOAuthMemberException();
    private NotOAuthMemberException() {
        super("2023", "소셜 로그인 정보가 없는 회원입니다.");
    }
}
