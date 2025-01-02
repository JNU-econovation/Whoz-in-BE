package com.whoz_in.main_api.config.security.oauth2;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.event.MemberOAuthInfoRegistered;
import com.whoz_in.domain.member.exception.NoMemberException;
import com.whoz_in.domain.member.model.Member;
import com.whoz_in.domain.member.model.OAuthCredentials;
import com.whoz_in.domain.member.model.SocialProvider;
import com.whoz_in.main_api.command.shared.application.CommandHandler;
import com.whoz_in.main_api.config.security.oauth2.response.ProviderResponse;
import com.whoz_in.main_api.config.security.oauth2.response.ProviderResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    // TODO: QueryHandler 나 다른 객체로 변경
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerName = userRequest.getClientRegistration().getRegistrationId();
        SocialProvider socialProvider = SocialProvider.findSocialProvider(providerName);
        ProviderResponse providerResponse = ProviderResponseFactory.create(socialProvider, oAuth2User.getAttributes());
        String socialId = providerResponse.getSocialId();
        String email = providerResponse.getEmail();
        String name = providerResponse.getName();
        boolean registered = memberRepository.existsBySocialProviderAndSocialId(socialProvider, socialId);
        // TODO: 일반 회원가입으로 등록이 되었을 경우에, 카카오 로그인을 시도하면 socialProvider 정보와 socialId 값을 저장해야 함
        // 카카오톡으로부터 사용자의 실명을 가져오면?

        if(!registered) register(name, socialProvider, socialId);

        return new OAuth2UserInfo(registered, socialProvider, socialId, email);
    }

    private void register(String name, SocialProvider socialProvider, String socialId) {
        try {
            // 이름을 기준으로 사용자 조회 후, OAuth 데이터만 삽입
            Member member = memberRepository.getByName(name);

            MemberOAuthInfoRegistered event =
                    new MemberOAuthInfoRegistered(
                            member.getId(),
                            OAuthCredentials.load(socialProvider, socialId));

            eventPublisher.publishEvent(event);

        } catch (NoMemberException e) {
            // 회원가입을 하고 OAuth 데이터를 삽입
            Member member = Member.create(name, OAuthCredentials.load(socialProvider, socialId));
            memberRepository.save(member);

        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred", e);
        }

    }


}
