package com.whoz_in.main_api.shared.jwt.tokens;

import java.time.Duration;
import java.time.Instant;
import lombok.Getter;

@Getter
public final class OAuth2TempToken extends Token{
    private final String userInfoKey;

    public OAuth2TempToken(String userInfoKey, Duration ttl) {
        this(userInfoKey, Instant.now().plus(ttl));
    }

    OAuth2TempToken (String userInfoKey, Instant expiredAt) {
        super(expiredAt);
        this.userInfoKey = userInfoKey;
    }

}
