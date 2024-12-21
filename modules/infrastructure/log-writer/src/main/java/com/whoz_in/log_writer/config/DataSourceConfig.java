package com.whoz_in.log_writer.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration("logWriterDataSourceConfig")
@RequiredArgsConstructor
public class DataSourceConfig {
    @Getter
    @Setter
    public static class DataSourceProperties {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }

    @Bean
    @ConfigurationProperties("log-writer.datasource")
    public DataSourceProperties logWriterDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource logWriterDataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("LogWriterHikariPool");
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());
        return dataSource;
    }

    @Bean
    public JdbcTemplate logWriterJdbcTemplate(@Qualifier("logWriterDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // 트랜잭션 매니저 - 아직은 쓸모 없는거 같아서 등록 안했음
    @Bean("logWriterTM")
    public PlatformTransactionManager logWriterTransactionManager(@Qualifier("logWriterDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
