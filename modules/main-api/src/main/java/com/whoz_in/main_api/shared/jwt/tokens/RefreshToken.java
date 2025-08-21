package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.MemberId;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;

@Getter
public final class RefreshToken extends Token {
    private final MemberId memberId;
    private final UUID tokenId; //블랙 리스트 등에 사용될 수 있음

    // 새로운 토큰 생성
    public RefreshToken(MemberId memberId, Duration ttl) {
        this(memberId, UUID.randomUUID(), Instant.now().plus(ttl));
    }

    // 기존 토큰으로 생성 (접근 제한자를 default로 둬서 외부에선 tokenId 설정 불가능하도록 함)
    RefreshToken(MemberId memberId, UUID tokenId, Instant expiredAt) {
        super(expiredAt);
        this.memberId = memberId;
        this.tokenId = tokenId;
    }
}
