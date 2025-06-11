package com.whoz_in.main_api.shared.jwt.tokens;


import static com.whoz_in.main_api.shared.jwt.JwtConst.MEMBER_ID;
import static com.whoz_in.main_api.shared.jwt.JwtConst.TOKEN_ID;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.shared.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public final class RefreshTokenSerializer extends TokenSerializer<RefreshToken> {
    //TODO: AutoConstructor
    public RefreshTokenSerializer(JwtUtil jwtUtil) {
        super(jwtUtil);
    }

    @Override
    protected RefreshToken buildToken(Claims claims) {
        MemberId memberId = new MemberId(UUID.fromString(claims.get(MEMBER_ID, String.class)));
        UUID tokenId = UUID.fromString(claims.get(TOKEN_ID, String.class));
        return new RefreshToken(memberId, tokenId, claims.getExpiration().toInstant());
    }

    @Override
    protected Map<String, String> buildClaims(RefreshToken refreshToken) {
        return Map.of(
                MEMBER_ID, refreshToken.getMemberId().toString(),
                TOKEN_ID, refreshToken.getTokenId().toString()
        );
    }

    @Override
    protected void validate(Claims claims) {
        super.validate(claims);
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.REFRESH;
    }
}
