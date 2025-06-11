package com.whoz_in.main_api.shared.jwt.tokens;

import java.time.Instant;
import lombok.Getter;

@Getter
public abstract class Token {
    private final Instant expiredAt;

    protected Token(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }
}
