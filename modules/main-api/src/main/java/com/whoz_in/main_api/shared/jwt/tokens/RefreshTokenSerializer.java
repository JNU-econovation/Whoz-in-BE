package com.whoz_in.main_api.shared.jwt.tokens;


import static com.whoz_in.main_api.config.security.consts.JwtConst.TOKEN_ID;
import static com.whoz_in.main_api.config.security.consts.JwtConst.USER_ID;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.JwtUtil;
import com.whoz_in.main_api.shared.jwt.TokenType;
import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public final class RefreshTokenSerializer extends TokenSerializer<RefreshToken> {
    //TODO: AutoConstructor
    public RefreshTokenSerializer(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        super(jwtUtil, jwtProperties);
    }

    @Override
    protected RefreshToken buildToken(Claims claims) {
        MemberId memberId = new MemberId(UUID.fromString(claims.get(USER_ID, String.class)));
        UUID tokenId = UUID.fromString(claims.get(TOKEN_ID, String.class));
        return new RefreshToken(memberId, tokenId);
    }

    @Override
    protected Map<String, String> buildClaims(RefreshToken refreshToken) {
        return Map.of(
                USER_ID, refreshToken.getMemberId().toString(),
                TOKEN_ID, refreshToken.getTokenId().toString()
        );
    }

    @Override
    protected void validate(Claims claims) {
        //TODO: claims.get(TOKEN_ID, String.class); 블랙리스트에 있으면 예외 던지기
        super.validate(claims);
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.REFRESH;
    }
}
