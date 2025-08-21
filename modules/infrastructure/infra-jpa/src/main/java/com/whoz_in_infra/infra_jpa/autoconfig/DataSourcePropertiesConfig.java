package com.whoz_in_infra.infra_jpa.autoconfig;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourcePropertiesConfig {

    /**
     * {@link DataSourceProperties}를 빈으로 등록함
     * @Bean 메서드를 통해 자동으로 빈 등록이 되기 때문에 @ConfigurationPropertiesScan 없이도 동작
     */
    @Bean
    @ConfigurationProperties("infra-jpa.datasource")
    public DataSourceProperties dataSourceProperties(){
        return new DataSourceProperties();
    }
}
