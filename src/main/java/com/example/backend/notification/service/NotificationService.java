package com.example.backend.notification.service;

import com.example.backend.notification.dto.NotificationRequestDto;
import com.example.backend.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final EmitterRepository emitterRepository;

    public void sendNotification(Long id, NotificationRequestDto requestDto) {
        if (requestDto == null) {
            return;
        }
        SseEmitter.SseEventBuilder sseEvent = SseEmitter.event()
                .id(id.toString())
                .name("sse")
                .data(requestDto.getBody());

        emitterRepository.get(id).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(sseEvent);
            } catch (IOException | IllegalStateException e) {
                emitterRepository.remove(id);
            }
        }, () -> log.info("hi"));

    }

}
