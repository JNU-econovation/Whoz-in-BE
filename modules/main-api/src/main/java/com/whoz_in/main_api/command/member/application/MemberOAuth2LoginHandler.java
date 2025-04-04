package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.member.model.AccountType;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.service.MemberFinderService;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.shared.jwt.tokens.AccessToken;
import com.whoz_in.main_api.shared.jwt.tokens.RefreshToken;
import com.whoz_in.main_api.shared.jwt.tokens.TokenSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;


/**
 * {@link com.whoz_in.main_api.config.security.oauth2.LoginSuccessHandler}에서 사용 중
 */
@Handler
@RequiredArgsConstructor
public class MemberOAuth2LoginHandler implements CommandHandler<MemberOAuth2Login, LoginSuccessTokens> {
    private final MemberFinderService memberFinderService;
    private final TokenSerializer<AccessToken> accessTokenSerializer;
    private final TokenSerializer<RefreshToken> refreshTokenSerializer;

    @Transactional(readOnly = true) //아직은 readOnly
    @Override
    public LoginSuccessTokens handle(MemberOAuth2Login cmd) {
        Member member = memberFinderService.findBySocialId(cmd.socialId());

        String accessToken = accessTokenSerializer.serialize(
                new AccessToken(member.getId(), AccountType.USER));
        String refreshToken = refreshTokenSerializer.serialize(
                new RefreshToken(member.getId()));

        return new LoginSuccessTokens(accessToken, refreshToken);
    }
}
