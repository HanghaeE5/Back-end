package com.example.backend.notification.controller;

import com.example.backend.msg.MsgEnum;
import com.example.backend.notification.domain.Notification;
import com.example.backend.notification.domain.Review;
import com.example.backend.notification.service.NotificationService;
import com.example.backend.user.common.LoadUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * @title 로그인 한 유저 sse 연결
     */
    @GetMapping(value = "/subscribe/{email}", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> subscribe(
            @PathVariable String email,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        SseEmitter sseEmitter = notificationService.subscribe(email, lastEventId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(sseEmitter);
    }

    @GetMapping("/subscribe/test")
    public ResponseEntity<String> subscribeTest() {
        notificationService.send("happygimy97@naver.com", new Review(123L, "리뷰에용"), "이제 되게찌?");
        return ResponseEntity.status(HttpStatus.OK)
                .body("안농?");
    }
}