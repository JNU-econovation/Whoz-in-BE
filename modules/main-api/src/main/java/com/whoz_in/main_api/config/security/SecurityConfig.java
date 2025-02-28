package com.whoz_in.main_api.config.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

//security와 관련된 빈을 등록하는 클래스
@Configuration
public class SecurityConfig {

    // Filter를 구현한 클래스가 빈으로 등록되면 전역 필터로 동작하기 때문에 모든 요청에 대해 jwt 검증을 수행하게 됩니다.
    // 따라서 필요한 시큐리티 필터 체인에서만 jwt 필터를 사용할 수 있도록 FilterRegistrationBean을 사용하여 전역 필터에서 제외합니다.
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistration(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(jwtAuthenticationFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    //위와 동일한 이유로 추가
    @Bean
    public FilterRegistrationBean<ServerAuthenticationFilter> serverAuthenticationFilterRegistration(
            ServerAuthenticationFilter serverAuthenticationFilter) {
        FilterRegistrationBean<ServerAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(serverAuthenticationFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    //위와 동일한 이유로 추가
    @Bean
    public FilterRegistrationBean<UnknownEndpointFilter> unknownEndpointFilterFilterRegistrationBean(
            UnknownEndpointFilter unknownEndpointFilter) {
        FilterRegistrationBean<UnknownEndpointFilter> registrationBean = new FilterRegistrationBean<>(unknownEndpointFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //security에서 사용할 Cors 설정을 정의합니다
    @Bean
    public CorsConfigurationSource corsConfigurationSource(
            @Value("${frontend.main.base-url}") String mainBaseUrl,
            @Value("${frontend.network-api.base-urls}") List<String> networkBaseUrls) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        Set<String> corsUrls = new HashSet<>(networkBaseUrls);
        corsUrls.add(mainBaseUrl);
        corsConfiguration.setAllowedOrigins(List.copyOf(corsUrls));
        corsConfiguration.setAllowedMethods(List.of("*"));
        corsConfiguration.setAllowedHeaders(List.of("*"));
        corsConfiguration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
