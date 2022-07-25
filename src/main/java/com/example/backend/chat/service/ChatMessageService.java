package com.example.backend.chat.service;

import com.example.backend.chat.domain.*;
import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.chat.dto.response.ChatMessageResponseDto;
import com.example.backend.chat.redis.RedisPub;
import com.example.backend.chat.redis.RedisRepository;
import com.example.backend.chat.repository.ChatMessageRepository;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.chat.repository.ParticipantRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService2 chatMessageService2;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;
    private final ParticipantRepository participantRepository;
    private final RedisPub redisPub;

    @Transactional
    public void sendChatMessage(ChatMessageRequestDto message, String email) {
        log.info("chat.service.ChatMessageService.sendChatMessage()");
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom room = chatRoomRepository.findById(message.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        if (message.getType() == MessageType.QUIT) {
            if (room.getType() == Type.PRIVATE) {
                message.setSender("[알림]");
                message.setMessage(user.getUsername() + "님이 채팅방을 나가셨습니다. 새로운 채팅방에서 채팅을 진행해 주세요!");
            } else {
                message.setSender("[알림]");
                message.setMessage(user.getUsername() + "님이 채팅방을 나가셨습니다");
            }
        }
        this.saveChatMessage(message);
        // 중간에 ResponseDto 로 변경하는 부분 필요 -> 지금은 LocalDateTime 직렬화 오류 현상 때문에 생략
        log.info("chat.service.ChatMessageService.sendChatMessage().end");
        redisPub.publish(redisRepository.getTopic(room.getRoomId()), message);
    }

    // 페이징으로 받아서 무한 스크롤 가능할듯
    public Page<ChatMessageResponseDto> getSavedMessages(String roomId, String email) {
        log.info("chat.service.ChatMessageService.getSavedMessages()");
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        Participant participant = participantRepository.findByUserAndChatRoom(user, chatRoom).orElseThrow(
                () -> new CustomException(ErrorCode.PARTICIPANT_NOT_FOUND)
        );
        Pageable pageable = PageRequest.of(0, 100, Sort.by("createdDate").descending());
        Page<ChatMessage> messagePage = chatMessageRepository.findAllByRoomId(pageable, roomId);
        Page<ChatMessageResponseDto> responseDtoPage = messagePage.map(ChatMessageResponseDto::new);
        for (ChatMessageResponseDto c : responseDtoPage) {
            if (c.getCreatedDate().isAfter(participant.getExitTime())) {
                c.read();
            }
        }
        return responseDtoPage;
    }

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

}
