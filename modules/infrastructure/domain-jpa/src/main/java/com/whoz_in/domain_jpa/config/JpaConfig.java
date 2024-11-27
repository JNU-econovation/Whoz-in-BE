package com.whoz_in.domain_jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaAuditing
//기본적으로 SpringApplication 모듈에서 찾기 때문에 이 모듈에서도 찾을 수 있도록 함
@EnableJpaRepositories(basePackages = {"com.whoz_in.domain_jpa"})
@EntityScan(basePackages = "com.whoz_in")
public class JpaConfig {
}
