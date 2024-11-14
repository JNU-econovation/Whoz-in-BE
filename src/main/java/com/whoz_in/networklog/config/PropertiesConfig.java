package com.whoz_in.networklog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({@PropertySource("classpath:/env.properties")})
public class PropertiesConfig {
}
