package com.whoz_in.main_api.shared.jwt;

import static com.whoz_in.main_api.config.security.consts.JwtConst.ACCESS_TOKEN;
import static com.whoz_in.main_api.config.security.consts.JwtConst.OAUTH2_LOGIN_TOKEN;
import static com.whoz_in.main_api.config.security.consts.JwtConst.OAUTH2_TEMP_TOKEN;
import static com.whoz_in.main_api.config.security.consts.JwtConst.REFRESH_TOKEN;

import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2LoginToken;
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
    OAUTH2_LOGIN_INFO(OAuth2LoginToken.class, OAUTH2_LOGIN_TOKEN),
    OAUTH2_TEMP(OAuth2TempToken.class, OAUTH2_TEMP_TOKEN);

    private final Class<? extends Token> tokenClass;
    //TODO: 웹에 종속되었다! 더 깔끔한 방법이 없을까..
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
