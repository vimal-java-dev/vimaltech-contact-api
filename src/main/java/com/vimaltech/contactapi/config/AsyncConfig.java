package com.vimaltech.contactapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class AsyncConfig {

    @Bean(name = "emailExecutor")
    public Executor emailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);       // baseline threads (good for VPS)
        executor.setMaxPoolSize(8);        // peak load
        executor.setQueueCapacity(100);    // queue size
        executor.setThreadNamePrefix("EmailThread-");
        executor.initialize();
        return executor;
    }
}