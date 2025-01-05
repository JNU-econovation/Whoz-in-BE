package com.whoz_in.main_api.config.security;

import com.whoz_in.main_api.config.security.oauth2.ClientRegistrationRepositoryFactory;
import com.whoz_in.main_api.config.security.oauth2.CustomOAuth2UserService;
import com.whoz_in.main_api.config.security.oauth2.LoginFailureHandler;
import com.whoz_in.main_api.config.security.oauth2.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                "/oauth2/authorization/*", //소셜 로그인 페이지 (OAuth2LoginConfigurer에서 자동 생성)
                "/login/oauth2/code/*"  //redirect uri
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

