package com.whoz_in.main_api.shared.jwt.tokens;


// TokenId 를 저장하는 인터페이스
public interface RefreshTokenStore {
    boolean saveIfAbsent(RefreshToken refreshToken);
}
