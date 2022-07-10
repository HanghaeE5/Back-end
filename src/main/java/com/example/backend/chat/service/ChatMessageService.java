package com.example.backend.chat.service;

import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.Participant;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.chat.repository.ParticipantRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final SimpMessageSendingOperations messageSendingOperations;
    private final ParticipantRepository participantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

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
        if (ChatMessageRequestDto.MessageType.ENTER.equals(message.getType())
                || ChatMessageRequestDto.MessageType.QUIT.equals(message.getType())) {
            message.setSender("[알림]");
        }
        messageSendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
