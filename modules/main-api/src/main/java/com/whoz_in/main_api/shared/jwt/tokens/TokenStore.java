package com.whoz_in.main_api.shared.jwt.tokens;

// TokenId 를 저장하는 인터페이스
// 하위 모듈에서 구현한다.
public interface TokenStore {

    void save(String tokenId);

    boolean isExist(String tokenId);

    void clear();

    void delete(String tokenId);

}
