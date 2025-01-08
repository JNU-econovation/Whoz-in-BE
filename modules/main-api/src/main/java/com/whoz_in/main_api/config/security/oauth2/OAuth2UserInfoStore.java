package com.whoz_in.main_api.config.security.oauth2;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//TODO: 패키지 위치 변경

// 소셜 로그인하며 받아온 사용자의 정보를 임시 저장하는 클래스
// 후즈인 소셜 회원가입 시에 꺼내서 사용할 수 있다.
// 키는 OAuthTempToken에 담겨 사용자에게 넘어간다.
@Component
@RequiredArgsConstructor
public class OAuth2UserInfoStore {
    private static final Cache<UUID, OAuth2UserInfo> store = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.MINUTES) // 5분 후 만료
                .maximumSize(20) // 최대 20개 항목 저장
                .build();

    public String save(OAuth2UserInfo userInfo) {
        UUID key = UUID.randomUUID();
        store.put(key, userInfo);
        return key.toString();
    }

    public OAuth2UserInfo takeout(String key) {
        UUID uuidKey = UUID.fromString(key);
        OAuth2UserInfo userInfo = store.getIfPresent(uuidKey);
        store.invalidate(uuidKey);
        if (userInfo == null) {
            throw new IllegalArgumentException("소셜 로그인 정보를 찾을 수 없습니다.");
        }
        return userInfo;
    }

}
