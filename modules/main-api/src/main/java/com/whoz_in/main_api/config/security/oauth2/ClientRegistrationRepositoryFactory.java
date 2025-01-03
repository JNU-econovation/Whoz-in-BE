package com.whoz_in.main_api.config.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientRegistrationRepositoryFactory {

    private final ClientRegistrations clientRegistrations;

    public ClientRegistrationRepository create(){
        return new InMemoryClientRegistrationRepository(
                clientRegistrations.kakaoClientRegistration()
        );
    }

}
