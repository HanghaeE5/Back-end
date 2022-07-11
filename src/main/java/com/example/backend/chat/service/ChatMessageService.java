package com.example.backend.chat.service;

import com.example.backend.chat.domain.ChatMessage;
import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.MessageType;
import com.example.backend.chat.domain.Participant;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final ParticipantRepository participantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public void addParticipant(String email, String roomId) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        Participant participant = new Participant(user, chatRoom);
        participantRepository.save(participant);
        chatRoom.addParticipant(participant);
        user.addParticipant(participant);
    }


    @Transactional
    public void deleteParticipant(String email, String roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        Participant participant = new Participant();
        for (Participant p : room.getParticipantList()) {
            if (Objects.equals(p.getUser().getEmail(), email)) {
                participant = p;
                break;
            }
        }
        participantRepository.deleteById(participant.getId());
    }


    public void sendChatMessage(ChatMessageRequestDto message) {
        if (MessageType.ENTER.equals(message.getType())
                || MessageType.QUIT.equals(message.getType())) {
            message.setSender("[알림]");
        }

        log.info("getMessage : " + message.getMessage());
        log.info("message.getRoomId() : " + message.getRoomId());
        log.info("getSender : " + message.getSender());
        System.out.println(message);

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
