package com.example.backend.chat;

import com.example.backend.user.token.AuthToken;
import com.example.backend.user.token.AuthTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final AuthTokenProvider tokenProvider;

    // websocket 요청 시 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String tokenStr = accessor.getFirstNativeHeader("Authorization");
        AuthToken token = tokenProvider.convertAuthToken(tokenStr);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            System.out.println("여긴 StompHandler validate 전");
            token.validate();
            System.out.println("여긴 StompHandler validate 후");
        }
        return message;
    }
}
