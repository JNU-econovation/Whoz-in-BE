package com.whoz_in.domain.member.exception;

import com.whoz_in.shared.WhozinException;

public class NotAuthMemberException extends WhozinException {
    public static final NotAuthMemberException EXCEPTION = new NotAuthMemberException();
    private NotAuthMemberException() {
        super("2022", "일반 로그인 정보가 없는 회원입니다.");
    }
}
