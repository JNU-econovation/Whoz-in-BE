package com.whoz_in.domain.member.exception;

import com.whoz_in.domain.shared.BusinessException;

public class NotOAuthMemberException extends BusinessException {
    public static final NotOAuthMemberException EXCEPTION = new NotOAuthMemberException();
    private NotOAuthMemberException() {
        super("2023", "소셜 로그인 정보가 없는 회원입니다.");
    }
}
