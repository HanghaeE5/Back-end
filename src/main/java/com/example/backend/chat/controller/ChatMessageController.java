package com.example.backend.chat.controller;

import com.example.backend.chat.domain.ChatMessage;
import com.example.backend.chat.repository.ParticipantRepository;
import com.example.backend.chat.service.ChatMessageService;
import com.example.backend.user.common.LoadUser;
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
    private final ChatMessageService chatMessageService;
    private final AuthTokenProvider tokenProvider;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message, @Header("Authorization") String tokenStr) {
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        String name = token.getTokenClaims().getId();

        // 입장, 퇴장 시 Participant 에 추가
        if (ChatMessage.MessageType.ENTER.equals((message.getType()))) {
            chatMessageService.addParticipant(LoadUser.getEmail(), message.getRoomId());
            message.setMessage(name + "님이 입장했습니다");
        } else if (ChatMessage.MessageType.QUIT.equals((message.getType()))) {
            chatMessageService.deleteParticipant(LoadUser.getEmail(), message.getRoomId());
            message.setMessage(name + "님이 퇴장했습니다");
        } else {
            message.setSender(name);
        }

        chatMessageService.sendChatMessage(message);
    }

}
