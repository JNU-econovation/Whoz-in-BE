package com.whoz_in.main_api.command.api.member.exception;

import com.whoz_in.shared.WhozinException;

public class NotFoundSocailLoginInfoException extends WhozinException {
    public static final NotFoundSocailLoginInfoException EXCEPTION = new NotFoundSocailLoginInfoException();
    private NotFoundSocailLoginInfoException() {
        super("2028", "소셜 로그인 정보를 찾을 수 없습니다.");
    }
}
