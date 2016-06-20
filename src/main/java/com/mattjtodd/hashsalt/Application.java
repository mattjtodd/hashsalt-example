package com.mattjtodd.hashsalt;

import javaslang.control.Try;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.support.ExecutorServiceAdapter;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.security.SecureRandom;
import java.util.concurrent.*;
import java.util.function.IntFunction;

@SpringBootApplication
@EnableAsync
public class Application implements AsyncConfigurer {

    static final String ENCODING_EXECUTOR = "encodingExecutor";

    ;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public IntFunction<byte[]> randomSauce() {
        SecureRandom secureRandom = Try
            .of(() -> SecureRandom.getInstance("NativePRNGNonBlocking"))
            .recoverWith(thrown -> Try.of(() -> SecureRandom.getInstance("SHA1PRNG", "Sun")))
            .getOrElse(SecureRandom::new);

        return lengthBytes -> generate(lengthBytes, secureRandom);

    }

    @Bean(name = ENCODING_EXECUTOR)
    public ExecutorService getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        int size = Runtime.getRuntime().availableProcessors() + 1;
        taskExecutor.setMaxPoolSize(size);
        taskExecutor.setCorePoolSize(size);
        taskExecutor.setKeepAliveSeconds(240);
        taskExecutor.setThreadNamePrefix("Hash-Encoder-");
        taskExecutor.initialize();
        ExecutorServiceAdapter adapter = new ExecutorServiceAdapter(taskExecutor);
        return new WaitTimeMonitoringExecutorService(adapter);
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    private static byte[] generate(int length, SecureRandom secureRandom) {
        byte[] salt = new byte[length];
        secureRandom.nextBytes(salt);
        return salt;
    }
}
