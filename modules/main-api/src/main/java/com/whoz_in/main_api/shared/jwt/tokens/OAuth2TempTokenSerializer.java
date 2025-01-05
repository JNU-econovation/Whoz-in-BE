package com.whoz_in.main_api.shared.jwt.tokens;

import static com.whoz_in.main_api.shared.jwt.JwtConst.OAUTH_USER_INFO_KEY;

import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.JwtUtil;
import com.whoz_in.main_api.shared.jwt.TokenType;
import io.jsonwebtoken.Claims;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class OAuth2TempTokenSerializer extends TokenSerializer<OAuth2TempToken> {

    public OAuth2TempTokenSerializer(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        super(jwtUtil, jwtProperties);
    }

    @Override
    protected OAuth2TempToken buildToken(Claims claims) {
        String userInfoKey = claims.get(OAUTH_USER_INFO_KEY, String.class);

        return new OAuth2TempToken(userInfoKey);
    }

    @Override
    protected Map<String, String> buildClaims(OAuth2TempToken jwtInfo) {
        return Map.of(
                OAUTH_USER_INFO_KEY, jwtInfo.getUserInfoKey()
        );
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.OAUTH2_TEMP;
    }
}
