package com.whoz_in.main_api.shared.utils;


import com.whoz_in.shared.WhozinException;

/*
필터에서 엔드포인트마다 적절한 인증 로직을 구현하면 발생하지 않음
 */
//TODO: 패키지 위치 변경
public class UserNotAuthenticationException extends WhozinException {

    protected UserNotAuthenticationException() {
        super("2025", "유저 아이디가 없습니다");
    }
}
