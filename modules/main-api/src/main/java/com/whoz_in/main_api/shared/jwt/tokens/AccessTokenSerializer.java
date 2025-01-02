package com.whoz_in.main_api.shared.jwt.tokens;


import static com.whoz_in.main_api.config.security.consts.JwtConst.ACCOUNT_TYPE;
import static com.whoz_in.main_api.config.security.consts.JwtConst.USER_ID;

import com.whoz_in.domain.member.model.AccountType;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.JwtUtil;
import com.whoz_in.main_api.shared.jwt.TokenType;
import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public final class AccessTokenSerializer extends TokenSerializer<AccessToken> {
    public AccessTokenSerializer(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        super(jwtUtil, jwtProperties);
    }

    @Override
    protected AccessToken buildToken(Claims claims) {
        MemberId memberId = new MemberId(UUID.fromString(claims.get(USER_ID, String.class)));
        AccountType accountType = AccountType.findAccountType(claims.get(ACCOUNT_TYPE, String.class));
        return new AccessToken(memberId, accountType);
    }

    @Override
    public Map<String, String> buildClaims(AccessToken accessToken) {
        return Map.of(
                USER_ID, accessToken.getUserId().toString()
        );
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.ACCESS;
    }
}
