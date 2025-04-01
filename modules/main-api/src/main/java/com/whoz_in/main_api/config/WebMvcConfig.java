package com.whoz_in.main_api.config;

import com.whoz_in.main_api.shared.presentation.TokenArgumentResolver;
import com.whoz_in.main_api.shared.presentation.logging.RequestLoggingInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final TokenArgumentResolver tokenArgumentResolver;
    private final RequestLoggingInterceptor requestLoggingInterceptor;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(tokenArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestLoggingInterceptor);
    }
}
