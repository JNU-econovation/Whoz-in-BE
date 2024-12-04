package com.whoz_in.log_writer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration("logWriterPropertiesConfig")
@PropertySource("classpath:/env-log-writer.properties")
public class PropertiesConfig {
}
