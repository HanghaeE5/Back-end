package com.example.backend.notification.controller;

import com.example.backend.msg.MsgEnum;
import com.example.backend.notification.domain.Member;
import com.example.backend.notification.domain.Notification;
import com.example.backend.notification.domain.Review;
import com.example.backend.notification.service.NotificationService;
import com.example.backend.user.common.LoadUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * @title 로그인 한 유저 sse 연결
     */
    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public ResponseEntity<String> subscribe(
            @PathVariable String id,
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId
    ) {
        LoadUser.loginAndNickCheck();
        notificationService.subscribe(id, lastEventId, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("application", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.SSE_CONNECT_SUCCESS.getMsg());
    }
    @PostMapping(value = "/reviews")
    public void sendReview(@RequestBody Notification notification, @RequestParam String content) {
        Member member = new Member();
        member.setId("7");
        Review review = new Review();
        review.setReview("하이여");
        review.setId("1");
        notificationService.send(member, review, content);
    }
}