package com.example.backend.chat.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final RedisSub redisSub;
    private final RedisMessageListenerContainer redisMessageListenerContainer;
    private final RedisTemplate<String, Object> redisTemplate;
    private Map<String, ChannelTopic> topicMap;

    @PostConstruct
    private void init() {
        topicMap = new HashMap<>();
    }

    // 채팅방 생성 시 사용해야함
    public void subscribe(String roomId) {
        log.info("chat.redis.RedisRepository.subscribe()");
        ChannelTopic topic = topicMap.get(roomId);
        if (topic == null) {
            topic = ChannelTopic.of(roomId);
            redisMessageListenerContainer.addMessageListener(redisSub, topic);
            topicMap.put(roomId, topic);
        }
    }

    public ChannelTopic getTopic(String roomId) {
        log.info("chat.redis.RedisRepository.getTopic()");
        return topicMap.get(roomId);
    }

}
