package com.example.backend.alarm.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Slf4j
public class AlarmScheduler {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final RedissonClient redissonClient;


    @Transactional
    @Scheduled(cron = "*/30 * * * * *")
    public void changeTime() {
        RLock lock = redissonClient.getLock("hide");
        try {
            boolean isLocked = lock.tryLock(4, 5, TimeUnit.SECONDS);
            if (!isLocked) {
                log.info("LOCK 획득 실패");
            }

            log.info("가즈아!");
            applicationEventPublisher.publishEvent("몰라몰라");

        } catch (InterruptedException e) {
            log.error(e.toString());
        } finally {
            lock.unlock();
        }

    }
}