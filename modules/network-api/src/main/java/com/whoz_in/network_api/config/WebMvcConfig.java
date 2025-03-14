package com.whoz_in.network_api.config;

import com.whoz_in.network_api.config.interceptor.ClientIpInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


//시큐리티가 cors를 설정하면 mvc의 cors는 무시된다.
//따라서 master-api로 실행될 경우 시큐리티를 사용하는 main-api의 cors 설정이 적용됨
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final ClientIpInterceptor clientIpInterceptor;

    public WebMvcConfig(ClientIpInterceptor clientIpInterceptor) {
        this.clientIpInterceptor = clientIpInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(clientIpInterceptor)
                .addPathPatterns("/**");
    }

}
