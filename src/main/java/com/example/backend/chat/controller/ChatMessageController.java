package com.example.backend.chat.controller;

import com.example.backend.chat.domain.ChatMessage;
import com.example.backend.user.token.AuthToken;
import com.example.backend.user.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final AuthTokenProvider tokenProvider;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("Authorization") String tokenStr) {
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        log.info("@MessageRequest");
        String name = token.getTokenClaims().getId();

        message.setSender(name);

        if (ChatMessage.MessageType.ENTER.equals((message.getType()))) {
            message.setMessage(name + "님이 입장했습니다");
        }

        messageSendingOperations.convertAndSend("/sub/chat/room" + message.getRoomId(), message);
    }
}
