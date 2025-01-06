package com.whoz_in.main_api.config.security.oauth2;

import com.whoz_in.domain.member.model.SocialProvider;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

// 우리 서비스에서 OAuth2 제공자로부터 받은 사용자 정보를 구성하는 클래스
@Getter
public class OAuth2UserInfo implements OAuth2User {

    private final boolean isRegistered; //후즈인에 회원가입이 되어있는지
    private final SocialProvider socialProvider;
    private final String socialId;

    public OAuth2UserInfo(boolean isRegistered, SocialProvider socialProvider, String socialId) {
        this.isRegistered = isRegistered;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
    }

    @Override
    public String getName() {
        return getSocialId();
    }

    @Override
    public String toString() {
        return String.format("%b-%s-%s", isRegistered, socialProvider, socialId);
    }

    //없애고 싶다.........
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }
    // TODO: 일단 빈 리스트, 추후 권한 기능 생기면 변경

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }
}
