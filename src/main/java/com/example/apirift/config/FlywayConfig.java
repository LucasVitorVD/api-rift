package com.example.apirift.config;

import org.springframework.beans.factory.annotation.Value;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {
    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @Bean(initMethod = "migrate", destroyMethod = "clean")
    public Flyway flyway(DataSource dataSource) {
        return Flyway.configure()
                .dataSource(dataSourceUrl, dataSourceUsername, dataSourcePassword)
                .locations("classpath:db/migration")
                .load();
    }
}