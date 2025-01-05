package com.whoz_in.main_api.config.security.oauth2;

import com.whoz_in.domain.member.MemberRepository;
import com.whoz_in.domain.member.model.SocialProvider;
import com.whoz_in.domain.shared.event.EventBus;
import com.whoz_in.main_api.config.security.oauth2.response.ProviderResponse;
import com.whoz_in.main_api.config.security.oauth2.response.ProviderResponseFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String providerName = userRequest.getClientRegistration().getRegistrationId();
        SocialProvider socialProvider = SocialProvider.findSocialProvider(providerName);
        ProviderResponse providerResponse = ProviderResponseFactory.create(socialProvider, oAuth2User.getAttributes());
        String socialId = providerResponse.getSocialId();
        String name = providerResponse.getName();
        boolean registered = memberRepository.existsBySocialProviderAndSocialId(socialProvider, socialId);
        return new OAuth2UserInfo(registered, socialProvider, socialId, name);
    }

}
