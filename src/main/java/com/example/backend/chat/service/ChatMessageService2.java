package com.example.backend.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// StompHandler 에 ChatMessageService DI 시 The dependencies of some of the beans in the application context form a cycle 오류
// 원인 : ChatMessageService 에는 SimpMessageSendingOperations 가 선언되어 있음
@Service
@RequiredArgsConstructor
public class ChatMessageService2 {

    public void read() {

    }

    public void enter() {

    }

    public void exit() {

    }

}
