package com.whoz_in.main_api.command.member.application.token;

// RefreshToken 을 저장하는 인터페이스
// 하위 모듈에서 구현한다.
public interface RefreshTokenStore {

    void save(String refreshTokenId);

}
