package com.whoz_in.main_api.config;

import com.whoz_in.main_api.config.security.oauth2.ClientRegistrationRepositoryFactory;
import com.whoz_in.main_api.config.security.oauth2.CustomOAuth2UserService;
import com.whoz_in.main_api.config.security.oauth2.LoginFailureHandler;
import com.whoz_in.main_api.config.security.oauth2.LoginSuccessHandler;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final ClientRegistrationRepositoryFactory clientRegistrationRepositoryFactory;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    @Bean
    @Order(0)
    public SecurityFilterChain oauth2FilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/login", //TODO: 운용에선 제거
                "/oauth2/authorization/*",
                "/login/oauth2/code/*"
        );

        commonConfigurations(httpSecurity);

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.oauth2Login(oauth2->
                oauth2
                        //.loginPage(null) //TODO: 운영에선 추가
                        .clientRegistrationRepository(clientRegistrationRepositoryFactory.create())
                        .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
        );

        return httpSecurity.build();
    }

    private void commonConfigurations(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(AbstractHttpConfigurer::disable);
        httpSecurity.requestCache(AbstractHttpConfigurer::disable);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

