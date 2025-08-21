package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.member.model.AccountType;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.jwt.JwtProperties;
import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenException;
import com.whoz_in.main_api.shared.jwt.tokens.TokenSerializer;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshTokenStore;
import com.whoz_in.main_api.shared.jwt.tokens.TokenType;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


@Handler
@RequiredArgsConstructor
public class ReissueHandler implements CommandHandler<Reissue, LoginSuccessTokens> {
    private final JwtProperties jwtProperties;
    private final TokenSerializer<RefreshToken> rtSerializer;
    private final TokenSerializer<AccessToken> atSerializer;
    private final RefreshTokenStore refreshTokenStore;

    @Override
    public LoginSuccessTokens handle(Reissue command) {
        RefreshToken rt = command.refreshToken();
        String memberId = rt.getMemberId().toString();

        if (refreshTokenStore.saveIfAbsent(rt)){
            RefreshToken newRt = new RefreshToken(
                    new MemberId(UUID.fromString(memberId)),
                    jwtProperties.getTokenExpiry(TokenType.REFRESH));
            AccessToken newAt = new AccessToken(
                    new MemberId(UUID.fromString(memberId)),
                    AccountType.USER,
                    jwtProperties.getTokenExpiry(TokenType.ACCESS)
            );

            String newRtString = rtSerializer.serialize(newRt);
            String newAtString = atSerializer.serialize(newAt);
            return new LoginSuccessTokens(newAtString, newRtString);
        }

        throw new TokenException("2005", "이미 사용한 리프레시 토큰입니다.");
    }
}
