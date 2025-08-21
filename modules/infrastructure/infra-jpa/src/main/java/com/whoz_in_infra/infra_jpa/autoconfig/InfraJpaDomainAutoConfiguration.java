package com.whoz_in_infra.infra_jpa.autoconfig;

import com.whoz_in_infra.infra_jpa.domain.config.DomainConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnProperty(prefix = "infra-jpa.domain", name = "enabled", havingValue = "true")
@Import(DomainConfig.class)
public class InfraJpaDomainAutoConfiguration {
}
