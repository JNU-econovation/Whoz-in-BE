package com.whoz_in.api_query_jpa.config;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.whoz_in.api_query_jpa",
        entityManagerFactoryRef = "apiQueryJpaEMF",
        transactionManagerRef = "apiQueryJpaTM"
)
public class ApiQueryJpaConfig {
    //DB 연결 세팅값 객체
    //잠깐쓸거라 밖으로 빼지 않았음
    //설정마다 필드가 다를 수 있으므로 공통 모듈로 빼지 않았음
    @Getter
    @Setter
    public static class DataSourceProperties {
        private String url;
        private String username;
        private String password;
        private String driverClassName;
    }
    //하이버네이트 관련 설정값 객체
    @Getter
    @Setter
    public static class HibernateProperties {
        private String ddlAuto;
        private String physicalNamingStrategy;
        private boolean formatSql;
        private boolean showSql;
    }

    //세팅값 객체를 빈으로 등록함
    //@Bean 메서드를 통해 자동으로 빈 등록이 되기 때문에 @ConfigurationPropertiesScan 없이도 동작함
    @Bean
    @ConfigurationProperties("api-query-jpa.datasource")
    public DataSourceProperties apiQueryJpaDataSourceProperties(){
        return new DataSourceProperties();
    }
    @Bean
    @ConfigurationProperties("api-query-jpa.hibernate")
    public HibernateProperties apiQueryJpaHibernateProperties(){
        return new HibernateProperties();
    }

    //세팅값을 통해 DataSource 생성
    @Bean
    public DataSource apiQueryJpaDataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("ApiQueryJpaHikariPool");
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());
        return dataSource;
    }

    //JPA 설정
    @Bean("apiQueryJpaEMF")
    public LocalContainerEntityManagerFactoryBean apiQueryJpaEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("apiQueryJpaDataSource") DataSource dataSource,
            @Qualifier("apiQueryJpaHibernateProperties") HibernateProperties hibernateProperties) {
        return builder
                .dataSource(dataSource) //DataSource 지정
                .persistenceUnit("api_query_jpa") //이름
                .packages("com.whoz_in.api_query_jpa") //JPA 엔티티가 존재하는 패키지
                .properties(
                        Map.of(
                                "hibernate.show_sql", hibernateProperties.showSql,
                                "hibernate.format_sql", hibernateProperties.formatSql,
                                "hibernate.ddl.auto", hibernateProperties.ddlAuto,
                                "hibernate.physical_naming_strategy", hibernateProperties.physicalNamingStrategy
                        )
                )
                .build();
    }

    @Bean("apiQueryJpaTM")
    public PlatformTransactionManager apiQueryJpaTransactionManager(
            @Qualifier("apiQueryJpaEMF") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
