package com.whoz_in.main_api.config.security.oauth2;

import com.whoz_in.domain.member.model.SocialProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;

@Component
public class ClientRegistrations {

    @Value("${oauth.kakao.secret}")
    private String kakaoSecret;

    @Value("${oauth.kakao.clientId}")
    private String kakaoClientId;

    @Value("${oauth.redirect-uri}")
    private String redirectUri;

    public ClientRegistration kakaoClientRegistration() {
        String providerName = SocialProvider.KAKAO.getProviderName();

        return ClientRegistration.withRegistrationId(providerName)
                .clientId(kakaoClientId)
                .clientSecret(kakaoSecret)
                .redirectUri(redirectUri)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .scope("profile_image")
                .authorizationUri("https://kauth.kakao.com/oauth/authorize")
                .tokenUri("https://kauth.kakao.com/oauth/token")
                .userInfoUri("https://kapi.kakao.com/v2/user/me")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .userNameAttributeName("id")
                .build();
    }

}

