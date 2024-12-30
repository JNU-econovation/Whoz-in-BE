package com.whoz_in.main_api.config.security.oauth2.response;

// Naver , Kakao , Google 등 OAuth2 제공자로부터 받는 응답 형식의 인터페이스
public interface ProviderResponse {

    String getSocialId();
    String getEmail();

}
