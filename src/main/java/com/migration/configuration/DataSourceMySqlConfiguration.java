package com.migration.configuration;

import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration to work with MySql Data Source
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.migration.repository.mysql",
    entityManagerFactoryRef = "mysqlEntityManagerFactory",
    transactionManagerRef = "mysqlTransactionManager")
public class DataSourceMySqlConfiguration {

    @Bean(name = "mysqlDataSourceProperties")
    @ConfigurationProperties("spring.mysql-data-source")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties("spring.mysql-data-source.configuration")
    public DataSource mysqlDataSource(
        @Qualifier("mysqlDataSourceProperties") DataSourceProperties primaryDataSourceProperties) {
        return primaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean("mysqlLiquibaseProperties")
    @ConfigurationProperties("spring.mysql-data-source.liquibase")
    public LiquibaseProperties mysqlLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean("mysqlLiquibase")
    public SpringLiquibase mysqlLiquibase(@Qualifier("mysqlDataSource") DataSource dataSource,
        @Qualifier("mysqlLiquibaseProperties") LiquibaseProperties liquibaseProperties) {
        final SpringLiquibase mysqlLiquibase = new SpringLiquibase();
        mysqlLiquibase.setDataSource(dataSource);
        mysqlLiquibase.setChangeLog(liquibaseProperties.getChangeLog());
        mysqlLiquibase.setContexts(liquibaseProperties.getContexts());
        mysqlLiquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        return mysqlLiquibase;
    }

    @Bean(name = "mysqlEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder mysqlEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean("mySqlJpaProperties")
    @ConfigurationProperties("spring.mysql-data-source.jpa")
    public JpaProperties mySqlJpaProperties() {
        return new JpaProperties();
    }

    @Bean(name = "mysqlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory(
        @Qualifier("mysqlEntityManagerFactoryBuilder") EntityManagerFactoryBuilder mysqlEntityManagerFactoryBuilder,
        @Qualifier("mysqlDataSource") DataSource primaryDataSource,
        @Qualifier("mySqlJpaProperties") JpaProperties jpaProperties
    ) {
        return mysqlEntityManagerFactoryBuilder
            .dataSource(primaryDataSource)
            .packages("com.migration.domain.entity.mysql")
            .persistenceUnit("mysqlDataSource")
            .properties(jpaProperties.getProperties())
            .build();
    }

    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(
        @Qualifier("mysqlEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }

}
