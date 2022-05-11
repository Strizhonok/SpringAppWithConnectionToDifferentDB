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
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Configuration to work with Postgres Data Source
 */
@Configuration
@EnableJpaRepositories(
    basePackages = "com.migration.repository.postgres",
    entityManagerFactoryRef = "postgresEntityManagerFactory",
    transactionManagerRef = "postgresTransactionManager")
public class DataSourcePostgresConfiguration {

    @Primary
    @Bean(name = "postgresDataSourceProperties")
    @ConfigurationProperties("spring.postgres-data-source")
    public DataSourceProperties postgresDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Primary
    @Bean(name = "postgresDataSource")
    @ConfigurationProperties("spring.postgres-data-source.configuration")
    public DataSource postgresDataSource(
        @Qualifier("postgresDataSourceProperties") DataSourceProperties primaryDataSourceProperties) {
        return primaryDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Primary
    @Bean("postgresLiquibaseProperties")
    @ConfigurationProperties("spring.postgres-data-source.liquibase")
    public LiquibaseProperties postgresLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Primary
    @Bean("postgresLiquibase")
    public SpringLiquibase postgresLiquibase(@Qualifier("postgresDataSource") DataSource dataSource,
        @Qualifier("postgresLiquibaseProperties") LiquibaseProperties liquibaseProperties) {
        final SpringLiquibase postgresLiquibase = new SpringLiquibase();
        postgresLiquibase.setDataSource(dataSource);
        postgresLiquibase.setChangeLog(liquibaseProperties.getChangeLog());
        postgresLiquibase.setContexts(liquibaseProperties.getContexts());
        postgresLiquibase.setDefaultSchema(liquibaseProperties.getDefaultSchema());
        return postgresLiquibase;
    }

    @Primary
    @Bean(name = "postgresEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder postgresEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Primary
    @Bean("postgresJpaProperties")
    @ConfigurationProperties("spring.postgres-data-source.jpa")
    public JpaProperties postgresJpaProperties() {
        return new JpaProperties();
    }

    @Primary
    @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(
        @Qualifier("postgresEntityManagerFactoryBuilder") EntityManagerFactoryBuilder postgresEntityManagerFactoryBuilder,
        @Qualifier("postgresDataSource") DataSource primaryDataSource,
        @Qualifier("postgresJpaProperties") JpaProperties jpaProperties
    ) {
        return postgresEntityManagerFactoryBuilder
            .dataSource(primaryDataSource)
            .packages("com.migration.domain.entity.postgres")
            .persistenceUnit("postgresDataSource")
            .properties(jpaProperties.getProperties())
            .build();
    }

    @Primary
    @Bean(name = "postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(
        @Qualifier("postgresEntityManagerFactory") EntityManagerFactory primaryEntityManagerFactory) {
        return new JpaTransactionManager(primaryEntityManagerFactory);
    }

}
