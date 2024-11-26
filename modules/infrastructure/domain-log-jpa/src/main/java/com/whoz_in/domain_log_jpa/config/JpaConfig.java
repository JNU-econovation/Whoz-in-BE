package com.whoz_in.domain_log_jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "com.whoz_in.common_domain_jpa")
public class JpaConfig {
}
