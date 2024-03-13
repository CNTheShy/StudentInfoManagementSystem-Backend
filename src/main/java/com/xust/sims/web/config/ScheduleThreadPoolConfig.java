package com.xust.sims.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.*;


@Configuration
@PropertySource("classpath:scheduledConfig.properties")
public class ScheduleThreadPoolConfig {
    @Value("${scheduledPoolSize}")
    private int poolSize;

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(poolSize);
    }
}
