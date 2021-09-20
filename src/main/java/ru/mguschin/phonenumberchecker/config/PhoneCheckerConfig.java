package ru.mguschin.phonenumberchecker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.Ordered;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingDeque;

@Configuration
@ComponentScan("ru.mguschin.phonenumberchecker.service")
public class PhoneCheckerConfig {

    @Bean
    public ExecutorService getThreadPool() {
        return new ThreadPoolExecutor(2, 10, 10, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    }
}
