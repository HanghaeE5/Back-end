package com.example.backend.chat.config.handler;

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

    // websocket 요청 시 실행
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("StompHandler preSend 접근");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT == accessor.getCommand()) {

            String tokenStr = accessor.getFirstNativeHeader("Authorization");
            AuthToken token = tokenProvider.convertAuthToken(tokenStr);
            log.info("연결됨");
            log.info("나는 websoket에서 받는 토큰이다 : "+token.getToken());
            token.validate();

        }

        // 일단 보류.. Controller 에서 같은 logic 다른 방식으로구현

//        else if (StompCommand.SUBSCRIBE == accessor.getCommand()) {
//
//            // header 정보에서 구독 destination 정보를 얻고, roomId를 추출한다.
//            String roomId = (String) message.getHeaders().get(StompHeaderAccessor.NATIVE_HEADERS, MultiValueMap.class).getFirst("roomId");
//
//            // 채팅방에 들어온 클라이언트 sessionId를 roomId와 맵핑해 놓는다.(나중에 특정 세션이 어떤 채팅방에 들어가 있는지 알기 위함)
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            chatRoomRepository.setUserEnterInfo(sessionId, roomId);
//
//            // 클라이언트 입장 메시지를 채팅방에 발송한다.(redis publish)
//            String name = Optional.ofNullable((Principal)message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
//            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.ENTER).roomId(roomId).sender(name).build());
//
//        } else if (StompCommand.UNSUBSCRIBE == accessor.getCommand()) {
//
//            // 연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
//            String sessionId = (String) message.getHeaders().get("simpSessionId");
//            String roomId = chatRoomRepository.getUserEnterRoomId(sessionId);
//
//            // 클라이언트 퇴장 메시지를 채팅방에 발송한다.(redis publish)
//            String name = Optional.ofNullable((Principal)message.getHeaders().get("simpUser")).map(Principal::getName).orElse("UnknownUser");
//            chatService.sendChatMessage(ChatMessage.builder().type(ChatMessage.MessageType.QUIT).roomId(roomId).sender(name).build());
//        }
        return message;
    }
}
