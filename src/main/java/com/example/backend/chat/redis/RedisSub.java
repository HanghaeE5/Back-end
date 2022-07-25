package com.example.backend.chat.redis;

import com.example.backend.chat.domain.ChatMessage;
import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.chat.dto.response.ChatMessageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSub implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messageSendingOperations;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("chat.redis.RedisSub.onMessage()");
        try {
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessageResponseDto chatMessage = objectMapper.readValue(publishMessage, ChatMessageResponseDto.class);
            messageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("chat.redis.RedisSub.onMessage.error");
        }
    }

}
