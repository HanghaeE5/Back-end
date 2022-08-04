package com.example.backend.notification.service;

import com.example.backend.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class EmitterService {

    private final EmitterRepository emitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60 * 24;

    public SseEmitter createEmitter(Long id) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitter.onCompletion(
                () -> emitterRepository.remove(id)
        );
        emitter.onTimeout(
                () -> emitterRepository.remove(id)
        );
        emitter.onError(e -> {
            emitterRepository.remove(id);
        });
        emitterRepository.addOrReplaceEmitter(id, emitter);
        return emitter;
    }
}