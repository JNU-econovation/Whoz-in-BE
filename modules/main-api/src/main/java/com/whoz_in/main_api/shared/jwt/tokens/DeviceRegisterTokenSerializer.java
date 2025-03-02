package com.whoz_in.main_api.shared.jwt.tokens;

import static com.whoz_in.main_api.shared.jwt.JwtConst.MEMBER_ID;

import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public final class DeviceRegisterTokenSerializer extends TokenSerializer<DeviceRegisterToken> {
    public DeviceRegisterTokenSerializer(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        super(jwtUtil, jwtProperties);
    }

    @Override
    protected DeviceRegisterToken buildToken(Claims claims) {
        MemberId memberId = new MemberId(UUID.fromString(claims.get(MEMBER_ID, String.class)));
        return new DeviceRegisterToken(memberId);
    }

    @Override
    public Map<String, String> buildClaims(DeviceRegisterToken deviceRegisterToken) {
        return Map.of(
                MEMBER_ID, deviceRegisterToken.getMemberId().toString()
        );
    }

    @Override
    protected TokenType getTokenType() {
        return TokenType.DEVICE_REGISTER;
    }

}
