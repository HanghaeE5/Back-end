package com.example.backend.chat.config;

import com.example.backend.chat.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@EnableWebSocketMessageBroker // 웹소켓 + Stomp 사용
@Configuration
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        System.out.println("웹소켓 컨피겨메세지브로코");
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub"); // @MessageRequest 로 라우팅

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        System.out.println("웹소켓 레지스터 스톰프엔드포인트");
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        System.out.println("여긴 웹소켓 config 의 configureClientInboundChannel");
        registration.interceptors(stompHandler);
    }

}
