package com.whoz_in.main_api.command.member.application;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.OAuthCredentials;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfo;
import com.whoz_in.main_api.shared.application.Handler;
import com.whoz_in.main_api.config.security.oauth2.OAuth2UserInfoStore;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2TempToken;
import com.whoz_in.main_api.shared.jwt.tokens.OAuth2TempTokenSerializer;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Handler
@RequiredArgsConstructor
public class MemberOAuth2SignUpHandler implements CommandHandler<MemberOAuth2SignUp, Void> {
    private final MemberRepository repository;
    private final OAuth2TempTokenSerializer oAuth2TempTokenSerializer;
    private final OAuth2UserInfoStore oAuth2UserInfoStore;
    private final EventBus eventBus;

    @Transactional
    @Override
    public Void handle(MemberOAuth2SignUp cmd) {
        OAuth2TempToken token = oAuth2TempTokenSerializer.deserialize(cmd.oAuth2TempToken()); //Token 역직렬화
        OAuth2UserInfo oAuth2UserInfo = oAuth2UserInfoStore.takeout(token.getUserInfoKey()); //Token이 가지는 key로 사용자의 정보를 꺼냄

        if (oAuth2UserInfo.isRegistered()) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }

        Member member = Member.create(cmd.name(), cmd.position(), cmd.generation(),
                OAuthCredentials.create(oAuth2UserInfo.getSocialProvider(), oAuth2UserInfo.getSocialId()));
        repository.save(member);
        eventBus.publish(member.pullDomainEvents());
        return null;
    }
}
