package com.whoz_in.main_api.shared.jwt;

import static com.whoz_in.main_api.shared.jwt.JwtConst.ACCESS_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH2_TEMP_TOKEN;
import static com.whoz_in.main_api.shared.jwt.JwtConst.REFRESH_TOKEN;

import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2TempToken;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshToken;
import com.whoz_in.main_api.shared.jwt.tokens.Token;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

//TODO: enums 로 옮기는건 어때
@Getter
@RequiredArgsConstructor
public enum TokenType {
    ACCESS(AccessToken.class, ACCESS_TOKEN),
    REFRESH(RefreshToken.class, REFRESH_TOKEN),
    OAUTH2_TEMP(OAuth2TempToken.class, OAUTH2_TEMP_TOKEN);

    private final Class<? extends Token> tokenClass;
    //TODO: 토큰 타입은 쿠키를 몰라야 한다.
    private final String cookieName;

    public static TokenType findByName(String tokenTypeName){
        for (TokenType tokenType : TokenType.values()){
            if (tokenType.name().equalsIgnoreCase(tokenTypeName))
                return tokenType;
        }
        throw new IllegalStateException("해당하는 토큰 없음");
    }

    public static TokenType findByClass(Class<?> tokenClass){
        for (TokenType tokenType : TokenType.values()){
            if (tokenType.tokenClass == tokenClass)
                return tokenType;
        }
        throw new IllegalStateException("해당하는 토큰 없음");
    }
}
