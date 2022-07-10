package com.example.backend.chat.service;

import com.example.backend.chat.dto.ChatMessageRequestDto;
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
        System.out.println("==========================3");
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        System.out.println("=======================4");
        Participant participant = new Participant(user, chatRoom);
        System.out.println("===========================5");
        participantRepository.save(participant);
        System.out.println("============================6");
        chatRoom.addParticipant(participant);
        System.out.println("========================7");
        user.addParticipant(participant);
    }


    public void deleteParticipant(String email, String roomId) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        Participant participant = new Participant();
        for (Participant p : room.getParticipantList()) {
            if (p.getUser().getEmail() == email) {
                participant = p;
                break;
            }
        }
        participantRepository.deleteById(participant.getId());
    }


    public void sendChatMessage(ChatMessageRequestDto message) {
        if (ChatMessageRequestDto.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 방에 입장했습니다.");
            message.setSender("[알림]");
        } else if (ChatMessageRequestDto.MessageType.QUIT.equals(message.getType())) {
            message.setMessage(message.getSender() + "님이 방에서 나갔습니다.");
            message.setSender("[알림]");
        }
        messageSendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomId(), message);
    }

}
