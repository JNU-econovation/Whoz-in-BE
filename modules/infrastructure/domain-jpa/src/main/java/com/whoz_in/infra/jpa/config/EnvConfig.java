package com.whoz_in.infra.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/env.properties")
public class EnvConfig {}
