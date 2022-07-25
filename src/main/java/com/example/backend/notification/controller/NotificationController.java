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
    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public ResponseEntity<SseEmitter> subscribe(
            @PathVariable Long id,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
//        LoadUser.loginAndNickCheck();
        SseEmitter sseEmitter = notificationService.subscribe(id, lastEventId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(sseEmitter);
    }

    @GetMapping("/subscribe/test")
    public ResponseEntity<String> subscribeTest() {
        notificationService.send(123L, new Review(123L, "리뷰에용"), "이제 되게찌?");
        return ResponseEntity.status(HttpStatus.OK)
                .body("안농?");
    }



}