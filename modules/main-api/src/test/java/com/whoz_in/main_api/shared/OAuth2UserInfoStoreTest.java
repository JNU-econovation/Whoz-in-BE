package com.whoz_in.main_api.shared;

import com.whoz_in.domain.member.model.SocialProvider;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfo;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfoStore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OAuth2UserInfoStoreTest {
    private final OAuth2UserInfoStore oAuth2UserInfoStore = new OAuth2UserInfoStore();
    private final OAuth2UserInfo testValue = new OAuth2UserInfo(false, SocialProvider.KAKAO, "12345");

    @Test
    @DisplayName("OAuth2UserInfo 저장/추출 테스트")
    void OAuth2UserInfo저장(){
        String key = oAuth2UserInfoStore.save(testValue);
        System.out.println("저장한 값 : " + testValue);
        System.out.println("저장한 키 : " + key);
        OAuth2UserInfo poped = oAuth2UserInfoStore.get(key);
        System.out.println("꺼낸 값 : " + poped);

        Assertions.assertEquals(testValue, poped);
    }

    @Test
    @DisplayName("OAuth2UserInfoStore 잘못된 키일 경우 테스트")
    void OAuth2UserInfoStoreInvalidKey(){
        Assertions.assertThrows(IllegalArgumentException.class, ()->oAuth2UserInfoStore.get("invalidKey"));
    }

}
