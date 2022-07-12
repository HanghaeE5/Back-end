package com.example.backend.chat.config.handler;

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
    private final ChatRoomService chatRoomService;

    // websocket 요청 시 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        log.info("StompHandler preSend 접근");

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String tokenStr = accessor.getFirstNativeHeader("Authorization");
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        token.validate();

        log.info("나는 websoket 에서 받는 토큰이다 : " + token.getToken());

        if (StompCommand.CONNECT == accessor.getCommand()) {

            log.info(message.getHeaders().toString());
            log.info("나는 StompCommand.CONNECT");

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {

            log.info(message.getHeaders().toString());
            log.info("나는 StompCommand.SUBSCRIBE");

        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
            // 채팅방 나가기 메서드 호출
//            chatRoomService.exitRoom(message, LoadUser.getEmail());
            log.info("나는 StompCommand.UNSUBSCRIBE");

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {

            log.info(message.getHeaders().toString());
            log.info("나는 StompCommand.DISCONNECT");

        }
        return message;
    }
}
