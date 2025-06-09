package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.MemberId;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public final class DeviceRegisterToken extends Token {
    @EqualsAndHashCode.Include
    private final UUID tokenId;
    private final MemberId memberId;

    public DeviceRegisterToken(MemberId memberId, Duration ttl) {
        this(UUID.randomUUID(), memberId, Instant.now().plus(ttl));
    }

    DeviceRegisterToken(UUID tokenId, MemberId memberId, Instant expiredAt) {
        super(expiredAt);
        this.tokenId = tokenId;
        this.memberId = memberId;
    }
}
