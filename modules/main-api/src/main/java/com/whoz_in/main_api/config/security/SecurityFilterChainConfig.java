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
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityFilterChainConfig {
    private final CustomOAuth2UserService customOAuth2UserService;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;
    private final ServerAuthenticationFilter serverAuthenticationFilter;
    private final AccessTokenFilter accessTokenFilter;
    private final DeviceRegisterTokenFilter deviceRegisterTokenFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UnknownEndpointFilter unknownEndpointFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    @Order(0)
    public SecurityFilterChain serverToServerFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/internal/**"
        );

        commonConfigurations(httpSecurity);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        //TODO: ip 화이트 리스트
        httpSecurity.addFilterAt(serverAuthenticationFilter, LogoutFilter.class);

        return httpSecurity.build();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain oauth2FilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/login", //시큐리티 기본 로그인 페이지
                "/oauth2/authorization/*", //소셜 로그인 페이지 (OAuth2LoginConfigurer에서 자동 생성)
                "/login/oauth2/code/*"  //redirect uri
        );

        commonConfigurations(httpSecurity);
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

    //인증인가 필요 없는 엔드포인트
    @Bean
    @Order(2)
    public SecurityFilterChain noAuthenticationFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher(
                "/api/v1/signup/oauth",
                "/api/v1/ssid"
        );

        commonConfigurations(httpSecurity);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.securityContext(AbstractHttpConfigurer::disable);
        return httpSecurity.build();
    }

    // 기기 등록 페이지에서 요청할 수 있는 api를 처리하는 필터
    @Bean
    @Order(3)
    public SecurityFilterChain deviceRegisterFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatchers(matcher ->
                matcher.requestMatchers(HttpMethod.POST,
                        "/api/v1/device/info",
                        "/api/v1/device"
                )
        ).authorizeHttpRequests(auth-> auth.anyRequest().authenticated());

        commonConfigurations(httpSecurity);

        httpSecurity.logout(AbstractHttpConfigurer::disable);
        httpSecurity.securityContext(AbstractHttpConfigurer::disable);
        //jwt(device register token)을 Authentication으로 만들어 등록하는 필터
        httpSecurity.addFilterAt(deviceRegisterTokenFilter, LogoutFilter.class);

        return httpSecurity.build();
    }

    //인증이 필요한 엔드포인트
    //로그아웃, 게시글 작성 등
    @Bean
    @Order(4)
    public SecurityFilterChain authenticationFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatchers(matcher->
                matcher.requestMatchers(HttpMethod.GET,
                        "/api/v1/device/info-status",
                        "/api/v1/devices",
                        "/api/v1/private-ips",
                        "/api/v1/members",
                        "/api/v1/member"
                ).requestMatchers(HttpMethod.POST,
                        "/api/v1/device-register-token",
                        "/api/v1/feedback"
                        //TODO: 로그아웃 추가
                ).requestMatchers(HttpMethod.PATCH,
                        "/api/v1/device/info"
                ).requestMatchers(HttpMethod.DELETE,
                        "/api/v1/device"
                )
        ).authorizeHttpRequests(auth-> auth.anyRequest().authenticated());

        commonConfigurations(httpSecurity);
        //쿠키를 받기 위한 설정
        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));
        //jwt(access token)을 Authentication으로 만들어 등록하는 필터
        httpSecurity.addFilterAt(accessTokenFilter, LogoutFilter.class);
        //인증 실패 핸들러
        httpSecurity.exceptionHandling(ex-> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint));

        return httpSecurity.build();
    }

    //인증 여부에 따라 다른 동작을 하는 엔드포인트 - 추가되면 주석 풀기
//    @Bean
//    @Order(5)
//    public SecurityFilterChain optionalAuthenticationFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.securityMatcher();
//
//        commonConfigurations(httpSecurity);
//        httpSecurity.logout(AbstractHttpConfigurer::disable);
//        //쿠키를 받기 위한 설정
//        httpSecurity.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));
//        //jwt(access token)을 Authentication으로 만들어 등록하는 필터
//        httpSecurity.addFilterAt(accessTokenFilter, LogoutFilter.class);
//        return httpSecurity.build();
//    }

    @Bean
    @Order(6)
    public SecurityFilterChain unknownFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.securityMatcher("/**");

        commonConfigurations(httpSecurity);
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        //서버가 처리할 수 있는 엔드포인트인지 확인하는 필터
        httpSecurity.addFilterBefore(unknownEndpointFilter, DisableEncodeUrlFilter.class);
        return httpSecurity.build();
    }



    private void commonConfigurations(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(config-> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); //세션을 생성 및 사용하지 않는다.
        httpSecurity.requestCache(AbstractHttpConfigurer::disable);
    }
}

