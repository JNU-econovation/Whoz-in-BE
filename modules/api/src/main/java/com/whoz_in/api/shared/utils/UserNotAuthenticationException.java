package com.whoz_in.api.shared.utils;

import com.whoz_in.domain.shared.BusinessException;

/*
필터에서 엔드포인트마다 적절한 인증 로직을 구현하면 발생하지 않음
 */
public class UserNotAuthenticationException extends BusinessException {

    protected UserNotAuthenticationException() {
        super("1010", "유저 아이디가 없습니다");
    }
}
