package com.example.backend.chat.controller;

import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.chat.dto.response.ChatMessageResponseDto;
import com.example.backend.chat.service.ChatMessageService;
import com.example.backend.user.common.LoadUser;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.token.AuthToken;
import com.example.backend.user.token.AuthTokenProvider;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final AuthTokenProvider tokenProvider;
    private final UserRepository userRepository;

    @ApiOperation(value = "메세지 전송(/pub)")
    @MessageMapping("/chat/message")
    public void message(ChatMessageRequestDto message, @Header("Authorization") String tokenStr) {
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        String email = token.getTokenClaims().getSubject();
        chatMessageService.sendChatMessage(message, email);
    }

    // 페이징 처리해야해서 나중에 쿼리 파라미터로 size, page 값을 받으므로 roomId 도 똑같은 형태로 받은 것!!
    @ResponseBody
    @GetMapping("/chat/message/before")
    public ResponseEntity<Page<ChatMessageResponseDto>> getSavedMessages(
            @RequestParam String roomId
    ) {
        LoadUser.loginAndNickCheck();
        Page<ChatMessageResponseDto> responseDtoList = chatMessageService.getSavedMessages(roomId, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
