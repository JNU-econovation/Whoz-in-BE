package com.whoz_in.main_api.shared.jwt.tokens;


import static com.whoz_in.main_api.config.security.consts.JwtConst.EMAIL;
import static com.whoz_in.main_api.config.security.consts.JwtConst.SOCIAL_ID;
import static com.whoz_in.main_api.config.security.consts.JwtConst.SOCIAL_PROVIDER;

import com.whoz_in.domain.member.model.SocialProvider;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.JwtUtil;
import com.whoz_in.main_api.shared.jwt.TokenType;
import io.jsonwebtoken.Claims;
import java.util.Map;
import org.springframework.stereotype.Component;

//TODO: 블랙리스트에 있으면 예외
@Component
public final class OAuth2LoginTokenProvider extends TokenSerializer<OAuth2LoginToken> {

    public OAuth2LoginTokenProvider(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        super(jwtUtil, jwtProperties);
    }

    @Override
    protected OAuth2LoginToken buildToken(Claims claims) {
        SocialProvider socialProvider = SocialProvider.findSocialProvider(claims.get(SOCIAL_PROVIDER, String.class));
        String socialId = claims.get(SOCIAL_ID, String.class);
        String email = claims.get(EMAIL, String.class);
        return new OAuth2LoginToken(socialProvider, socialId, email);
    }

    @Override
    public Map<String, String> buildClaims(OAuth2LoginToken oAuth2LoginToken) {
        return Map.of(
                SOCIAL_PROVIDER, oAuth2LoginToken.getSocialProvider().getProviderName(),
                SOCIAL_ID, oAuth2LoginToken.getSocialId(),
                EMAIL, oAuth2LoginToken.getEmail()
        );
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.OAUTH2_LOGIN_INFO;
    }
}
