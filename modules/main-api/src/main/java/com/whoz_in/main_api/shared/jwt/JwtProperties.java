package com.whoz_in.main_api.shared.jwt;

import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    @Getter
    private final SecretKey secretKey;
    private final Map<TokenType, Duration> tokenExpiryDurations = new EnumMap<>(TokenType.class);

    public Duration getTokenExpiry(TokenType tokenType){
        return tokenExpiryDurations.get(tokenType);
    }

    @ConstructorBinding
    public JwtProperties(String secret,
            @NonNull Duration accessTokenExpiry,
            @NonNull Duration refreshTokenExpiry,
            @NonNull Duration oAuth2TempTokenExpiry,
            @NonNull Duration deviceRegisterTokenExpiry
    ) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
        tokenExpiryDurations.put(TokenType.ACCESS, accessTokenExpiry);
        tokenExpiryDurations.put(TokenType.REFRESH, refreshTokenExpiry);
        tokenExpiryDurations.put(TokenType.OAUTH2_TEMP, oAuth2TempTokenExpiry);
        tokenExpiryDurations.put(TokenType.DEVICE_REGISTER, deviceRegisterTokenExpiry);
        Arrays.stream(TokenType.values()).forEach(tokenType -> {
            if (!tokenExpiryDurations.containsKey(tokenType))
                throw new IllegalStateException("%s의 토큰 만료 기간 설정 안됨".formatted(tokenType));
        });
    }
}

