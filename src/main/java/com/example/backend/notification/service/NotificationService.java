package com.example.backend.notification.service;

import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.notification.domain.Notification;
import com.example.backend.notification.dto.NotificationRequestDto;
import com.example.backend.notification.dto.NotificationResponseDto;
import com.example.backend.notification.repository.EmitterRepository;
import com.example.backend.notification.repository.NotificationRepository;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public void sendNotification(Long id, NotificationRequestDto requestDto) {
        if (requestDto == null) {
            return;
        }
        User user = userRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        Map result = objectMapper.convertValue(requestDto, Map.class);
        System.out.println(result);
        SseEmitter.SseEventBuilder sseEvent = SseEmitter.event()
                .id(id.toString())
                .name("sse")
                .data(result);

        emitterRepository.get(id).ifPresentOrElse(sseEmitter -> {
            try {
                sseEmitter.send(sseEvent);
            } catch (IOException | IllegalStateException e) {
                emitterRepository.remove(id);
            }
        }, () -> log.info("sendNotification Error"));

        notificationRepository.save(new Notification(requestDto, user));
    }

    public List<NotificationResponseDto> getNotification(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        List<NotificationResponseDto> responseDtoList = new ArrayList<>();
        List<Notification> notificationList = notificationRepository.findByUser(user);
        for (Notification n : notificationList) {
            n.changeState();
            responseDtoList.add(new NotificationResponseDto(n));
        }
        return responseDtoList;
    }

}
