package com.example.backend.chat.service;

import com.example.backend.chat.domain.*;
import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.chat.dto.response.ChatMessageResponseDto;
import com.example.backend.chat.redis.RedisPub;
import com.example.backend.chat.redis.RedisRepository;
import com.example.backend.chat.repository.ChatMessageRepository;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.notification.dto.NotificationRequestDto;
import com.example.backend.notification.service.NotificationService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final RedisRepository redisRepository;
    private final RedisPub redisPub;
    private final NotificationService notificationService;

    @Transactional
    public void sendChatMessage(ChatMessageRequestDto message, String email) {
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
                message.setMessage(user.getUsername() + "님이 채팅방을 나가셨습니다.");
            }
        }
        // 중간에 ResponseDto 로 변경하는 부분 필요 -> 지금은 LocalDateTime 직렬화 오류 현상 때문에 생략
        room.newMessage();
        this.saveChatMessage(message);
        // 자신 제외 알림 전송
        List<User> userList = new ArrayList<>();
        for (Participant p : room.getParticipantList()) {
            userList.add(p.getUser());
        }
        for (User u : userList) {
            if (u == user) {
                continue;
            }
            String notification = room.getRoomId();
            NotificationRequestDto requestDto = new NotificationRequestDto(com.example.backend.notification.domain.Type.채팅, notification);
            notificationService.sendNotification(u.getUserSeq(), requestDto);
        }
        redisPub.publish(redisRepository.getTopic(room.getRoomId()), message);
    }

    @Transactional
    public void sendEnterMessage(ChatRoom room, User user) {
        ChatMessageRequestDto message = new ChatMessageRequestDto(room, user);
        room.newMessage();
        this.saveChatMessage(message);
        redisPub.publish(redisRepository.getTopic(room.getRoomId()), message);
    }

    @Transactional
    public void saveChatMessage(ChatMessageRequestDto message) {

        if (!Objects.equals(message.getSender(), "[알림]")) {
            User user = userRepository.findByUsername(message.getSender()).orElseThrow(
                    () -> new CustomException(ErrorCode.USER_NOT_FOUND)
            );
            chatMessageRepository.save(new ChatMessage(message, user));
        } else {
            chatMessageRepository.save(new ChatMessage(message));
        }

    }

    // 페이징으로 받아서 무한 스크롤 가능할듯
    public Page<ChatMessageResponseDto> getSavedMessages(String roomId) {

        Pageable pageable = PageRequest.of(0, 100, Sort.by("createdDate").descending());
        Page<ChatMessage> messagePage = chatMessageRepository.findAllByRoomId(pageable, roomId);
        return messagePage.map(ChatMessageResponseDto::new);

    }

}
