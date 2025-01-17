package com.whoz_in.domain.member.model;

import java.util.Arrays;
import lombok.Getter;

@Getter
public enum SocialProvider {
    KAKAO("kakao", true);

    private String providerName;
    private boolean registerable;

    SocialProvider(String socialProvider, boolean registerable) {
        this.providerName = socialProvider;
        this.registerable = registerable;
    }

    public static SocialProvider findSocialProvider(String socialProvider){
        return Arrays.stream(SocialProvider.values())
                .filter(provider -> provider.getProviderName().equals(socialProvider))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("no social provider"));
    }
    
}
