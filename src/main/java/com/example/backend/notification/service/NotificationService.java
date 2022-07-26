package com.example.backend.notification.service;

import com.example.backend.notification.domain.Notification;
import com.example.backend.notification.domain.Review;
import com.example.backend.notification.repository.EmitterRepository;
import com.example.backend.notification.repository.EmitterRepositoryImpl;
import com.example.backend.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final EmitterRepository emitterRepository = new EmitterRepositoryImpl();

    public SseEmitter subscribe(String userId, String lastEventId) {
        // 1
        String id = userId + "_" + System.currentTimeMillis();  // 7_currenTimeMillis() -> id

        // 2
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));   // 미수신 알림을 다 수신하면 삭제,,? -> 그러면 이 이후에 알림이 오는 것들은 어떻게 받는 거징
        emitter.onTimeout(() -> emitterRepository.deleteById(id));  // timout 되면 삭제,,?

        // 3
        // 503 에러를 방지하기 위한 더미 이벤트 전송 -> SSE 연결을 했을 때 데이터 전송이 이루어지지 않으면 503 에러 발생시켜
        sendToClient(emitter, id, "EventStream Created. [userId=" + userId + "]");

        // 4
        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(userId);
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }
        return emitter;

    }

    //알림을 보내는 메서드
    public void send(String receiver, Review review, String content) {
        Notification notification = createNotification(receiver, review, content);

        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(receiver);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, notification);
                }
        );
    }

    private Notification createNotification(String receiver, Review review, String content) {
        return Notification.builder()
                .receiver(receiver)
                .content(content)
                .url("/reviews/" + review.getId())
                .isRead(false)
                .build();
    }
    // 3
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException exception) {
            emitterRepository.deleteById(id);
            throw new RuntimeException("연결 오류!");
        }
    }
}