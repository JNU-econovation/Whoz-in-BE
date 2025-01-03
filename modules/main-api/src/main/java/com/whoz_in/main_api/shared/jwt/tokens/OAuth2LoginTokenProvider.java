package com.whoz_in.main_api.shared.jwt.tokens;


import static com.whoz_in.main_api.config.security.consts.JwtConst.OAUTH2_TOKEN_KEY;
import static com.whoz_in.main_api.config.security.consts.JwtConst.SOCIAL_PROVIDER;

import com.whoz_in.domain.member.model.SocialProvider;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.JwtUtil;
import com.whoz_in.main_api.shared.jwt.TokenType;
import com.whoz_in.main_api.shared.utils.OAuth2TokenStore;
import com.whoz_in.main_api.shared.utils.OAuth2TokenStore.OAuth2TokenKey;
import io.jsonwebtoken.Claims;
import java.util.Map;
import org.springframework.stereotype.Component;

//TODO: 블랙리스트에 있으면 예외
@Component
public final class OAuth2LoginTokenProvider extends TokenSerializer<OAuth2LoginToken> {

    public OAuth2LoginTokenProvider(JwtUtil jwtUtil,
                                    JwtProperties jwtProperties) {
        super(jwtUtil, jwtProperties);
    }

    @Override
    protected OAuth2LoginToken buildToken(Claims claims) {
        String hashedTokenKey = claims.get(OAUTH2_TOKEN_KEY, String.class);

        return OAuth2TokenStore.getSocialId(hashedTokenKey);
    }

    @Override
    public Map<String, String> buildClaims(OAuth2LoginToken oAuth2LoginToken) {
        return Map.of(
                OAUTH2_TOKEN_KEY, OAuth2TokenStore.save(oAuth2LoginToken)
        );
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.OAUTH2_LOGIN_INFO;
    }
}
