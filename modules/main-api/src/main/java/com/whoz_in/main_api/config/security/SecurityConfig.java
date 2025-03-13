package com.whoz_in.main_api.config.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//security와 관련된 빈을 등록하는 클래스
@Configuration
public class SecurityConfig {

    // Filter를 구현한 클래스가 빈으로 등록되면 전역 필터로 동작하기 때문에 모든 요청에 대해 jwt 검증을 수행하게 됩니다.
    // 따라서 필요한 시큐리티 필터 체인에서만 jwt 필터를 사용할 수 있도록 FilterRegistrationBean을 사용하여 전역 필터에서 제외합니다.
    @Bean
    public FilterRegistrationBean<AccessTokenFilter> accessTokenFilterRegistration(
            AccessTokenFilter accessTokenFilter) {
        FilterRegistrationBean<AccessTokenFilter> registrationBean = new FilterRegistrationBean<>(
                accessTokenFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }

    //위와 동일한 이유로 추가
    @Bean
    public FilterRegistrationBean<DeviceRegisterTokenFilter> deviceRegisterTokenFilterRegistration(
            DeviceRegisterTokenFilter deviceRegisterTokenFilter) {
        FilterRegistrationBean<DeviceRegisterTokenFilter> registrationBean = new FilterRegistrationBean<>(
                deviceRegisterTokenFilter);
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
}
