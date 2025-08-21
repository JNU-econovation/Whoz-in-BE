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
public final class DeviceRegisterTokenSerializer extends TokenSerializer<DeviceRegisterToken> {
    public DeviceRegisterTokenSerializer(JwtUtil jwtUtil) {
        super(jwtUtil);
    }

    @Override
    protected DeviceRegisterToken buildToken(Claims claims) {
        MemberId memberId = new MemberId(UUID.fromString(claims.get(MEMBER_ID, String.class)));
        UUID tokenId = UUID.fromString(claims.get(TOKEN_ID, String.class));
        return new DeviceRegisterToken(tokenId, memberId, claims.getExpiration().toInstant());
    }

    @Override
    public Map<String, String> buildClaims(DeviceRegisterToken deviceRegisterToken) {
        return Map.of(
                MEMBER_ID, deviceRegisterToken.getMemberId().toString(),
                TOKEN_ID, deviceRegisterToken.getTokenId().toString()
        );
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.DEVICE_REGISTER;
    }

}
