package com.example.backend.chat.redis;

import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.chat.dto.response.ChatMessageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPub {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageRequestDto message) {
        log.info("chat.redis.RedisPub.publish()");
        log.info("topic.getTopic() = " + topic.getTopic());
        log.info("ChatMessageResponseDto message = " + message);
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
