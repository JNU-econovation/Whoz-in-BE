package com.whoz_in.main_api.shared.jwt.tokens;

import java.time.Duration;
import java.time.Instant;

public final class OpenApiToken extends Token {

    public OpenApiToken(Duration ttl) {
        this(Instant.now().plus(ttl));
    }

    OpenApiToken(Instant expiredAt) {
        super(expiredAt);
    }
}
