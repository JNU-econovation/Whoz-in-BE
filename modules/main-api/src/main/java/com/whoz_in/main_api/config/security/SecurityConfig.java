package com.whoz_in.main_api.config.security;

import com.whoz_in.main_api.config.security.oauth2.CustomOAuth2UserService;
import com.whoz_in.main_api.config.security.oauth2.LoginFailureHandler;
import com.whoz_in.main_api.config.security.oauth2.LoginSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @Order(1)
    public SecurityFilterChain oauth2FilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/login", //시큐리티 기본 로그인 페이지
                "/oauth2/authorization/*", //소셜 로그인 페이지 (OAuth2LoginConfigurer에서 자동 생성)
                "/login/oauth2/code/*"  //redirect uri
        );

        commonConfigurations(httpSecurity);

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.oauth2Login(oauth2->
                oauth2
                        .clientRegistrationRepository(clientRegistrationRepository)
                        .userInfoEndpoint(config -> config.userService(customOAuth2UserService))
                        .successHandler(loginSuccessHandler)
                        .failureHandler(loginFailureHandler)
        );

        return httpSecurity.build();
    }

    //인증이 필요하거나 인증 여부에 따라 다른 동작을 하는 메서드
    //로그아웃, 게시글 작성 등
    @Bean
    @Order(3)
    public SecurityFilterChain authenticationFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/**"
        );
        httpSecurity.authorizeHttpRequests(auth-> {
            //인증 필요
            auth.requestMatchers(HttpMethod.POST,
                    "/api/v1/device"
            ).authenticated();
            //인증 여부에 따라 다른 동작
//            auth.requestMatchers(
//            ).permitAll();
            auth.anyRequest().denyAll();
        });

        commonConfigurations(httpSecurity);

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.addFilterAt(jwtAuthenticationFilter, LogoutFilter.class);
        //TODO: 로그아웃 추가

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

