package com.example.backend.notification.repository;

import com.example.backend.notification.domain.Notification;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
public interface EmitterRepository {
    Map<String, Object> findAllEventCacheStartWithId(String userId);
    SseEmitter save(String id, SseEmitter em);
    void deleteById(String id);

    Map<String, SseEmitter> findAllStartWithById(String id);

    void saveEventCache(String key, Notification notification);
}