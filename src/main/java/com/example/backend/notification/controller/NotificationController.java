package com.example.backend.notification.controller;

import com.example.backend.notification.dto.NotificationRequestDto;
import com.example.backend.notification.service.EmitterService;
import com.example.backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final EmitterService emitterService;
    private final NotificationService notificationService;

    @GetMapping("/subscribe/{id}")
    public SseEmitter subsribe(
            @PathVariable Long id
    ) {
        return emitterService.createEmitter(id);
    }

    @PostMapping("/publish/notification/{id}")
    public void publish(
            @PathVariable Long id,
            @RequestBody NotificationRequestDto requestDto
    ) {
        notificationService.sendNotification(id, requestDto);
    }
}
