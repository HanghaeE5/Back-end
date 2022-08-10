package com.example.backend.chat.config;

import com.example.backend.chat.service.ChatMessageService;
import com.example.backend.chat.service.ChatMessageService2;
import com.example.backend.user.token.AuthToken;
import com.example.backend.user.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ChannelInterceptorImpl implements ChannelInterceptor {

    private final AuthTokenProvider tokenProvider;
    private final ChatMessageService2 chatMessageService2;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {

            String tokenStr = accessor.getFirstNativeHeader("Authorization");
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);
            token.validate();

        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {

            String tokenStr = accessor.getFirstNativeHeader("Authorization");
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);
            token.validate();
            String email = token.getTokenClaims().getSubject();

            String roomId = chatMessageService2.getRoomId(Optional.ofNullable((String) message.getHeaders().get("simpDestination")));
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            // 채팅방 입장한 사람 수 ++
            // 입장한 userId 와 sessionId 맵핑
            chatMessageService2.plusParticipant(roomId);
            chatMessageService2.mapSessionAndParticipant(email, roomId, sessionId);

        } else if (StompCommand.DISCONNECT == accessor.getCommand()) {

            // Disconnect 시 sessionId 정보 획득 가능
            // Subscribe 시 맵핑 해뒀던 userId 로 맵핑을 없애고 해당 방의  roomId 정보 획득
            // roomId 로 해당 채팅방 입장한 사람 수 --
            String sessionId = (String) message.getHeaders().get("simpSessionId");
            String roomId = chatMessageService2.exitParticipant(sessionId);
            if (roomId != null) {
                chatMessageService2.minusParticipantCount(roomId);
            }

        }
        return message;
    }
}
