package com.whoz_in.main_api.config.security.oauth2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

@Component
public class ClientRegistrations {

    @Value("${oauth.kakao.secret}")
    private String KAKAO_SECRET;

    @Value("${oauth.kakao.clientId}")
    private String KAKAO_CLIENT_ID;

    @Value("${oauth.redirectUri}")
    private String redirectUri;

    public ClientRegistration kakaoClientRegistration() {

        return ClientRegistration.withRegistrationId("kakao")
                .clientId(KAKAO_CLIENT_ID)
                .clientSecret(KAKAO_SECRET)
                .redirectUri(redirectUri)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("account_email")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userNameAttributeName("id")
                .build();
    }

}

