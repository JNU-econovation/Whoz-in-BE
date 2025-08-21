package com.whoz_in_infra.infra_jpa.query.config;

import com.whoz_in_infra.infra_jpa.autoconfig.DataSourceProperties;
import com.whoz_in_infra.infra_jpa.shared.HibernateProperties;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManagerFactory;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@EnableJpaRepositories(
        basePackages = "com.whoz_in_infra.infra_jpa.query",
        entityManagerFactoryRef = "queryEMF",
        transactionManagerRef = "queryTM"
)
@ComponentScan("com.whoz_in_infra.infra_jpa.query")
public class QueryConfig {
    @Bean
    @ConfigurationProperties("infra-jpa.query.hibernate")
    public HibernateProperties queryHibernateProperties(){
        return new HibernateProperties();
    }

    @Bean
    public DataSource queryDataSource(DataSourceProperties properties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setPoolName("queryHikariPool");
        dataSource.setJdbcUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        dataSource.setDriverClassName(properties.getDriverClassName());
        return dataSource;
    }

    @Bean("queryEMF")
    public LocalContainerEntityManagerFactoryBean queryEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("queryDataSource") DataSource dataSource,
            @Qualifier("queryHibernateProperties") HibernateProperties hibernateProperties) {
        return builder
                .dataSource(dataSource)
                .persistenceUnit("query")
                .packages("com.whoz_in_infra.infra_jpa.query")
                .properties(
                        Map.of(
                                "hibernate.show_sql", hibernateProperties.isShowSql(),
                                "hibernate.format_sql", hibernateProperties.isFormatSql(),
                                "hibernate.hbm2ddl.auto", hibernateProperties.getDdlAuto(),
                                "hibernate.physical_naming_strategy", hibernateProperties.getPhysicalNamingStrategy()
                        )
                )
                .build();
    }

    @Bean("queryTM")
    public PlatformTransactionManager queryTransactionManager(
            @Qualifier("queryEMF") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
