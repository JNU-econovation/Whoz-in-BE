package com.whoz_in.main_api.command.member.application.command;

import com.whoz_in.domain.member.model.AccountType;
import com.whoz_in.domain.member.model.MemberId;
import com.whoz_in.main_api.shared.jwt.tokens.TokenStore;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenException;
import com.whoz_in.main_api.shared.jwt.tokens.TokenSerializer;
import java.util.UUID;
import lombok.RequiredArgsConstructor;


@Handler
@RequiredArgsConstructor
public class ReissueHandler implements CommandHandler<Reissue, LoginSuccessTokens> {

    private final TokenSerializer<RefreshToken> rtSerializer;
    private final TokenSerializer<AccessToken> atSerializer;
    private final TokenStore tokenStore;

    @Override
    public LoginSuccessTokens handle(Reissue command) {
        String tokenId = command.refreshTokenId();
        String memberId = command.memberId();

        if(!tokenStore.isExist(tokenId)){
            tokenStore.save(command.refreshTokenId());

            RefreshToken newRtObj = new RefreshToken(new MemberId(UUID.fromString(memberId)));
            AccessToken newAtObj = new AccessToken(new MemberId(UUID.fromString(memberId)), AccountType.USER);

            String newRtString = rtSerializer.serialize(newRtObj);
            String newAtString = atSerializer.serialize(newAtObj);

            return new LoginSuccessTokens(newAtString, newRtString);
        }

        throw new TokenException("2005", "이미 사용한 리프레시 토큰입니다.");
    }
}
