package com.whoz_in_infra.infra_jpa.domain.config;

import com.whoz_in_infra.infra_jpa.autoconfig.DataSourceProperties;
import com.whoz_in_infra.infra_jpa.shared.HibernateProperties;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

//@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
        basePackages = "com.whoz_in_infra.infra_jpa.domain",
        entityManagerFactoryRef = "domainEMF",
        transactionManagerRef = "domainTM"
)
@ComponentScan("com.whoz_in_infra.infra_jpa.domain")
@RequiredArgsConstructor
public class DomainConfig {
    @Bean
    @ConfigurationProperties("infra-jpa.domain.hibernate")
    public HibernateProperties domainHibernateProperties(){
        return new HibernateProperties();
    }

    @Primary
    @Bean
    public DataSource domainDataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("DomainHikariPool");
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());
        return dataSource;
    }

    @Primary
    @Bean("domainEMF")
    public LocalContainerEntityManagerFactoryBean domainEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("domainDataSource") DataSource dataSource,
            @Qualifier("domainHibernateProperties") HibernateProperties hibernateProperties) {
        return builder
                .dataSource(dataSource)
                .persistenceUnit("domain")
                .packages("com.whoz_in_infra.infra_jpa.domain") //JPA 엔티티가 존재하는 패키지
                .properties(
                        Map.of(
                            "hibernate.hbm2ddl.auto", hibernateProperties.getDdlAuto(),
                            "hibernate.physical_naming_strategy", hibernateProperties.getPhysicalNamingStrategy(),
                            "hibernate.show_sql", hibernateProperties.isShowSql(),
                            "hibernate.format_sql", hibernateProperties.isFormatSql()
                        )
                )
                .build();
    }

    @Primary
    @Bean("domainTM")
    public PlatformTransactionManager domainTransactionManager(
            @Qualifier("domainEMF") EntityManagerFactory entityManagerFactory) {
        //필요하면 설정 추가하기
        return new JpaTransactionManager(entityManagerFactory);
    }
}
