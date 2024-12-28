package com.whoz_in.domain.member.model;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum SocialProvider {
    KAKAO("kakao");

    private String socialProvider;

    SocialProvider(String socialProvider) {
        this.socialProvider = socialProvider;
    }

    public static SocialProvider findSocialProvider(String socialProvider){
        return Arrays.stream(SocialProvider.values())
                .filter(provider -> provider.getSocialProvider().equals(socialProvider))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("no social provider"));
    }


}
