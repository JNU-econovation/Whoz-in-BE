package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.AccountType;
import com.whoz_in.domain.member.model.MemberId;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public final class AccessToken extends Token {
    private final MemberId memberId;
    private final UUID tokenId;
    private final AccountType accountType;

    public AccessToken(MemberId memberId, AccountType accountType){
        this.memberId = memberId;
        this.accountType = accountType;
        this.tokenId = UUID.randomUUID();
    }

}
