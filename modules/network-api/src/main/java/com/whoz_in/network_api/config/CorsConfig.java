package com.whoz_in.network_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(DynamicCorsConfigurationSource dynamicSource) {
        return new CorsFilter(dynamicSource);
    }
}