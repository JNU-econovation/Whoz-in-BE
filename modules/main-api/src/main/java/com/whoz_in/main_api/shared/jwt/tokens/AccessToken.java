package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.AccountType;
import com.whoz_in.domain.member.model.MemberId;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class AccessToken extends Token {
    private final MemberId memberId;
    private final AccountType accountType;
}
