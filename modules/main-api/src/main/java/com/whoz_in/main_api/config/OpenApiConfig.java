package com.whoz_in.main_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .servers(List.of(
                        new Server().url("https://www.whozin.econovation.kr")
                                .description("운영 서버"),
                        new Server().url("http://localhost:2470")
                                .description("로컬 개발 서버")
                ));
    }

    private Info apiInfo() {
        return new Info()
                .title("Whoz-in-Swagger")
                .description("Whoz-in Service API")
                .version("1.0.0");
    }

}
