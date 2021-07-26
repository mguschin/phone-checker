package ru.mguschin.phonenumberchecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@ComponentScan("ru.mguschin.phonenumberchecker.service")
public class PhoneCheckerConfig {
/*
    @Bean
    public DataSource h2sqlDBSource() {

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:h2:mem:phonechecker");
        config.setUsername("sa");
        config.setPassword("");

        HikariDataSource ds = new HikariDataSource(config);

        return ds;
    }*/
}
