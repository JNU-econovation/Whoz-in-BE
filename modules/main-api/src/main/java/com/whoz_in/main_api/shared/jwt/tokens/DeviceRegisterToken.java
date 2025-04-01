package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.MemberId;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public final class DeviceRegisterToken extends Token {
    @EqualsAndHashCode.Include
    private final UUID tokenId;
    private final MemberId memberId;

    public DeviceRegisterToken(MemberId memberId) {
        this(UUID.randomUUID(), memberId);
    }
}
