package com.whoz_in.api.config;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

//Jackson의 ObjectMapper 설정을 변경하여 모든 응답의 필드가 스네이크 케이스로 보내지도록 합니다.
@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilder jacksonObjectMapperBuilder() {
        return new Jackson2ObjectMapperBuilder()
                .propertyNamingStrategy(SnakeCaseStrategy.INSTANCE);
    }
}