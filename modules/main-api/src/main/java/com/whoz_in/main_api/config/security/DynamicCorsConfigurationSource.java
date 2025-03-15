package com.whoz_in.main_api.config.security;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Slf4j
@Component
public class DynamicCorsConfigurationSource implements CorsConfigurationSource{
    private final List<String> allowedOrigins = new CopyOnWriteArrayList<>();

    public DynamicCorsConfigurationSource(
            @Value("${frontend.main.base-url}") String mainFrontendBaseUrl) {
        // 기본으로 허용할 origin
        allowedOrigins.add("http://localhost:3000");
        allowedOrigins.add(mainFrontendBaseUrl);
        log.info("등록된 cors origin: {}", allowedOrigins);
    }

    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.addAllowedHeader("*");
        config.setAllowedMethods(List.of("*"));
        config.setAllowCredentials(true);
        config.addExposedHeader("Set-Cookie");
        //        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        //        source.registerCorsConfiguration("/**", corsConfiguration);
        return config;
    }

    public void addAllowedOrigin(String origin) {
        if (!allowedOrigins.contains(origin)) {
            allowedOrigins.add(origin);
            log.info("추가된 후 origin 목록: {}", allowedOrigins);
        } else {
            log.warn("{}는 이미 origin 목록에 존재합니다.", origin);
        }
    }

    public boolean isAllowedOrigin(String origin) {
        return allowedOrigins.contains(origin);
    }

    public void removeAllowedOrigin(String origin) {
        if (allowedOrigins.remove(origin))
            log.warn("{}는 origin 목록에 존재하지 않습니다.", origin);
    }
}
