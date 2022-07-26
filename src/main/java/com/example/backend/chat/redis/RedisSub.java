package com.example.backend.chat.redis;

import com.example.backend.chat.domain.ChatMessage;
import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.chat.dto.response.ChatMessageResponseDto;
import com.example.backend.chat.repository.ChatMessageRepository;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.chat.service.ChatMessageService;
import com.example.backend.chat.service.ChatMessageService2;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSub implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final SimpMessageSendingOperations messageSendingOperations;
    private final ChatMessageService2 chatMessageService2;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public ChatMessage saveChatMessage(ChatMessageRequestDto message) {

        log.info("chat.service.ChatMessageService.saveChatMessage()");
        // if 문 안에서 participant 숫자로 read 숫자를 계산
        Long participantCount = chatMessageService2.getParticipantCount(message.getRoomId());
        log.info("chat.service.ChatMessageService.saveChatMessage().participantCount = " + participantCount);
        ChatRoom chatRoom = chatRoomRepository.findById(message.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        log.info("1111111111111111111111111111111111111111111111111111111");
        Long notRead = chatRoom.getParticipantList().size() - participantCount;
        log.info("chat.service.ChatMessageService.saveChatMessage().notRead = " + notRead);
        if (!Objects.equals(message.getSender(), "[알림]")) {
            log.info("2222222222222222222222222222222222222222222222222222222222222");
            User user = userRepository.findByUsername(message.getSender()).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_FOUND)
            );
            return chatMessageRepository.save(new ChatMessage(message, user, notRead));
        }
        else {
            log.info("33333333333333333333333333333333333333333333333333");
            return chatMessageRepository.save(new ChatMessage(message));
        }
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        log.info("chat.redis.RedisSub.onMessage()");
        try {
            String publishMessage = redisTemplate.getStringSerializer().deserialize(message.getBody());
            ChatMessageRequestDto chatMessage = objectMapper.readValue(publishMessage, ChatMessageRequestDto.class);
            ChatMessage saved = this.saveChatMessage(chatMessage);
            ChatMessageResponseDto responseDto = new ChatMessageResponseDto(saved);
            messageSendingOperations.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), responseDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            log.error("chat.redis.RedisSub.onMessage.error");
        }
    }

}
