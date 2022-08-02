package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 기본적으로 실행 대기 중인 Thread 개수
        executor.setMaxPoolSize(10); // 동시에 동작하는 최대 Thread 개수
        executor.setQueueCapacity(500); // CorePool이 초과될때 Queue에 저장했다가 꺼내서 실행된다. (500개까지 저장함)
        // 단, MaxPoolSize가 초과되면 Thread 생성에 실패할 수 있음.
        executor.setThreadNamePrefix("async-"); // Spring에서 생성하는 Thread 이름의 접두사
        executor.initialize();
        return executor;
    }
}