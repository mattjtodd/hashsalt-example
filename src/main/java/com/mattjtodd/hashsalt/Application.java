package com.mattjtodd.hashsalt;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.crypto.SecretKeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.function.Supplier;

@SpringBootApplication
@EnableAsync
public class Application implements AsyncConfigurer {

    static final String ENCODING_EXECUTOR = "encodingExecutor";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public SecretKeyFactory secretKeyFactory() {
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public Supplier<SecureRandom> randomSauce() {
        return SecureRandom::new;
    }

    @Bean(name = ENCODING_EXECUTOR)
    public TaskExecutor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(8);
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setKeepAliveSeconds(240);
        taskExecutor.setThreadNamePrefix("LULExecutor-");
        taskExecutor.initialize();
        return taskExecutor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}
