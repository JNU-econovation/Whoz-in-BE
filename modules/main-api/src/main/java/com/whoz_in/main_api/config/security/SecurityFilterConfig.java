package com.whoz_in.main_api.config.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityFilterConfig {

    // Filter를 구현한 클래스가 빈으로 등록되면 전역 필터로 동작하기 때문에 모든 요청에 대해 jwt 검증을 수행하게 됩니다.
    // 따라서 필요한 경우에만 jwt 필터를 사용할 수 있도록 FilterRegistrationBean을 사용하여 전역 필터에서 제외합니다.
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterRegistration(
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>(jwtAuthenticationFilter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }
}
