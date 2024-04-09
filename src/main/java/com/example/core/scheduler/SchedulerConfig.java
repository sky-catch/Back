package com.example.core.scheduler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class SchedulerConfig implements SchedulingConfigurer {
    private static final int SCHEDULER_THREAD_COUNT = 6;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }

    @Bean
    public Executor taskScheduler() {
        return Executors.newScheduledThreadPool(SCHEDULER_THREAD_COUNT);
    }
}
