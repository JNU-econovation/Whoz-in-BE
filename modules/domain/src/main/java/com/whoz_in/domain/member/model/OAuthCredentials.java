package com.whoz_in.domain.member.model;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//소셜 로그인 정보
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuthCredentials {
    private final SocialProvider socialProvider;
    private final String socialId;

    public static OAuthCredentials load(SocialProvider socialProvider, String socialId){
        if (socialProvider == null || socialId == null)
            throw new IllegalStateException("no social provider or social id");
        return new OAuthCredentials(socialProvider, socialId);
    }
}
