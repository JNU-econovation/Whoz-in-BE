package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.AccountType;
import com.whoz_in.domain.member.model.MemberId;
import java.time.Duration;
import java.time.Instant;
import lombok.Getter;

@Getter
public final class AccessToken extends Token {
    private final MemberId memberId;
    private final AccountType accountType;

    public AccessToken(MemberId memberId, AccountType accountType, Duration ttl) {
        this(memberId, accountType, Instant.now().plus(ttl));
    }

    AccessToken(MemberId memberId, AccountType accountType, Instant expiredAt) {
        super(expiredAt);
        this.memberId = memberId;
        this.accountType = accountType;
    }

}
