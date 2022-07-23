package com.example.backend.chat.redis;

import com.example.backend.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPub {

    private final RedisTemplate<?, ?> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage message) {
        log.info("chat.redis.RedisPub.publish()");
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }

}
