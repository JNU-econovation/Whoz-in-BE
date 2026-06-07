package com.whoz_in.main_api.shared.jwt.tokens;

import com.whoz_in.main_api.shared.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public final class OpenApiTokenSerializer extends TokenSerializer<OpenApiToken> {
    public OpenApiTokenSerializer(JwtUtil jwtUtil) {
        super(jwtUtil);
    }

    @Override
    protected OpenApiToken buildToken(Claims claims) {
        return new OpenApiToken(claims.getExpiration().toInstant());
    }

    @Override
    protected Map<String, String> buildClaims(OpenApiToken jwtInfo) {
        return Map.of();
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.OPEN_API;
    }
}
