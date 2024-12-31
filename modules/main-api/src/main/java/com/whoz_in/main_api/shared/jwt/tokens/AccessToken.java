package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.domain.member.model.AccountType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class AccessToken extends Token {
    private final Long userId;
    private final AccountType accountType;
}
