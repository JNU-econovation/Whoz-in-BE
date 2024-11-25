package com.whoz_in.domain_jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaAuditing
//멀티 모듈에서 명시적으로 엔티티와 JpaRepository의 위치를 지정하기 위함
@EnableJpaRepositories(basePackages = "com.whoz_in")
@EntityScan(basePackages = "com.whoz_in")
public class JpaConfig {
}
