package com.whoz_in.main_api.shared.jwt.tokens;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED) //tokenId를 외부에서 설정할 수 없도록 한다.
public final class RefreshToken extends Token {
    private final Long userId;
    private final UUID tokenId;

    public RefreshToken(Long userId) {
        this(userId, UUID.randomUUID());
    }
}
