package com.example.backend.notification.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {

    private Map<Long, SseEmitter> userEmitterMap = new ConcurrentHashMap<>();

    public void addOrReplaceEmitter(Long id, SseEmitter emitter) {
        userEmitterMap.put(id, emitter);
    }

    public void remove(Long id) {
        if (userEmitterMap != null) {
            userEmitterMap.remove(id);
        }
    }

    public Optional<SseEmitter> get(Long id) {
        return Optional.ofNullable(userEmitterMap.get(id));
    }
}
