package com.example.backend.chat.config.handler;

import com.example.backend.chat.service.ChatMessageService2;
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

import java.util.Optional;

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

        // Subscribe 시
        if (StompCommand.CONNECT == accessor.getCommand()) {

            log.info("나는 StompCommand.CONNECT");
            log.info(message.getHeaders().toString());

            String tokenStr = accessor.getFirstNativeHeader("Authorization");
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);
            token.validate();
            String email = token.getTokenClaims().getSubject();

            log.info("나는 websoket 에서 받는 토큰이다 : " + token.getToken());
            log.info("내 이메일은 " + email + "이다");
            log.info(message.getHeaders().toString());

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {

            log.info("나는 StompCommand.SUBSCRIBE");
            log.info(message.getHeaders().toString());

            // sessionId, userId 맵핑을 위해 user 필요
            String tokenStr = accessor.getFirstNativeHeader("Authorization");
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);
            token.validate();
            String email = token.getTokenClaims().getSubject();

            String roomId = chatMessageService2.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")));
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            // 채팅방 사람 수 ++
            chatMessageService2.plusParticipantCount(roomId);
            // sessionId 와 participant(user와 roomId로 찾을 수 있음) 맵핑 진행
            chatMessageService2.mapSessionAndParticipant(email, roomId, sessionId);

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {

            log.info("나는 StompCommand.DISCONNECT");
            log.info(message.getHeaders().toString());
            // disconnect 시 sessionId 정보
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            System.out.println(sessionId);
            // sessionId 로 맵핑된 participant 알 수 있음. 해당 participant 의 exitTime 변경
            String roomId = chatMessageService2.exitParticipant(sessionId);
            // participant 로 ChatRoom 의 roomId 알 수 있음. 해당 roomId 로 채팅방 사람 수 --
            chatMessageService2.minusParticipantCount(roomId);

        }
        return message;
    }
}
