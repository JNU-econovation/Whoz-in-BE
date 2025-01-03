package com.whoz_in.main_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilderFactory;

@Configuration
public class UtilsConfig {

    @Bean
    public UriBuilderFactory uriBuilderFactory() {
        return new DefaultUriBuilderFactory();
    }

}
