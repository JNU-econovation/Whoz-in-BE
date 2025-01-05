package com.whoz_in.main_api.shared.jwt.tokens;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2TempToken extends Token{

    private final String userInfoKey;

}
