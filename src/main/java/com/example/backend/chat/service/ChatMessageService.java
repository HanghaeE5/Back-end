package com.example.backend.chat.service;

import com.example.backend.chat.domain.ChatMessage;
import com.example.backend.chat.domain.MessageType;
import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.chat.dto.response.ChatMessageResponseDto;
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
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public void sendChatMessage(ChatMessageRequestDto message, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        message.setProfileImageUrl(user.getProfileImageUrl());
        this.saveChatMessage(message);
        messageSendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

    // 페이징으로 받아서 무한 스크롤 가능할듯
    public Page<ChatMessageResponseDto> getSavedMessages(String roomId) {
        Pageable pageable = PageRequest.of(0, 100, Sort.by("createdDate").descending());

        Page<ChatMessage> messagePage = chatMessageRepository.findAllByRoomId(pageable, roomId);
        return messagePage.map(ChatMessageResponseDto::new);
    }

    public void saveChatMessage(ChatMessageRequestDto message) {
        chatMessageRepository.save(new ChatMessage(message));
    }
}
