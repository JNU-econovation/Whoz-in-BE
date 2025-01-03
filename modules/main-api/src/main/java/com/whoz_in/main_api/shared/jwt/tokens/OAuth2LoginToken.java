package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.SocialProvider;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class OAuth2LoginToken extends Token {
    private final SocialProvider socialProvider;
    private final String socialId;
    private final String name;

    @Override
    public String toString() {
        return String.format("%s-%s-%s", socialProvider.name(), socialId, name);
    }
}
