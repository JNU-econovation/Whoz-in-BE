package com.whoz_in.main_api.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

// Jackson의 ObjectMapper 설정을 변경하여 전역적으로 역/직렬화 설정 변경
@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                // 속성명을 스네이크 케이스로 변경
                // 응답으로 스네이크로 넘기고 싶은데 애플리케이션 영역에서 Jackson 어노테이션을 사용하기 싫어서 설정했음
                .propertyNamingStrategy(SnakeCaseStrategy.INSTANCE);
    }
}