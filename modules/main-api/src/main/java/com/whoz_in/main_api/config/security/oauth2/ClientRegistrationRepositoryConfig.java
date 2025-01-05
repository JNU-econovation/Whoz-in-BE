package com.whoz_in.main_api.config.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

@Configuration
@RequiredArgsConstructor
public class ClientRegistrationRepositoryConfig {
    private final ClientRegistrations clientRegistrations;

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        return new InMemoryClientRegistrationRepository(
                clientRegistrations.kakaoClientRegistration()
        );
    }
}
