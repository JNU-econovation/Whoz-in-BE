package com.whoz_in.main_api.config.security.oauth2.response;

import java.util.Map;

public class KakaoResponse implements ProviderResponse{

    private final Map<String, Object> attributes;
    private final Map<String, Object> kakaoAccountAttributes;

    @SuppressWarnings("unchecked")
    public KakaoResponse(Map<String, Object> attributes) {
        this.attributes = attributes;
        this.kakaoAccountAttributes = (Map<String, Object>) attributes.get("kakao_account");
    }

    @Override
    public String getSocialId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return kakaoAccountAttributes.get("email").toString();
    }

    @SuppressWarnings("unchecked")
    @Override
    public String getName() {
        // 카카오톡 프로필에 입력된 사용자의 이름
        Map<String, Object> profle = (Map<String, Object>) kakaoAccountAttributes.get("profile");
        return profle.get("nickname").toString();
    }
}
