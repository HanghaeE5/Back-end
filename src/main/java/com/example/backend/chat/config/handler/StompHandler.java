package com.example.backend.chat.config.handler;

import com.example.backend.chat.service.ChatMessageService;
import com.example.backend.chat.service.ChatMessageService2;
import com.example.backend.chat.service.ChatRoomService;
import com.example.backend.user.common.LoadUser;
import com.example.backend.user.token.AuthToken;
import com.example.backend.user.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final AuthTokenProvider tokenProvider;
    private final ChatMessageService2 chatMessageService2;

    // websocket 요청 시 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        log.info("StompHandler preSend 접근");

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String tokenStr = accessor.getFirstNativeHeader("Authorization");
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        token.validate();
//        String email = token.getTokenClaims().getSubject();
//
//        log.info("내 이메일은 " + email + "이다");
        log.info("나는 websoket 에서 받는 토큰이다 : " + token.getToken());

        // Subscribe 시
        if (StompCommand.CONNECT == accessor.getCommand()) {

            log.info(message.getHeaders().toString());
            log.info("나는 StompCommand.CONNECT");

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {

            log.info(message.getHeaders().toString());
            log.info("나는 StompCommand.SUBSCRIBE");
            // subscribe 시 마지막으로 연결 끊은 시간 이후의 해당 채팅방 메세지를 읽음 처리
            // participant 의 status 를 true 변경
            chatMessageService2.read();
            chatMessageService2.enter();

        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {

            log.info("나는 StompCommand.UNSUBSCRIBE");

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {

            log.info(message.getHeaders().toString());
            log.info("나는 StompCommand.DISCONNECT");
            // participant 의 status 를 false 로 변경
            chatMessageService2.exit();

        }
        return message;
    }
}
