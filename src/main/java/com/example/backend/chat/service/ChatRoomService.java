package com.example.backend.chat.service;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.Participant;
import com.example.backend.chat.dto.ChatRoomPrivateRequestDto;
import com.example.backend.chat.dto.ChatRoomPublicRequestDto;
import com.example.backend.chat.dto.ChatRoomResponseDto;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.chat.repository.ParticipantRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    // 일대일 채팅은 일단 보류
    public ChatRoomResponseDto createPrivateRoom(ChatRoomPrivateRequestDto requestDto, String email) {
        ChatRoom room = new ChatRoom(requestDto);
        chatRoomRepository.save(room);
        return new ChatRoomResponseDto(room);
    }

    // 단체 톡방
    @Transactional
    public ChatRoomResponseDto createPublicRoom(ChatRoomPublicRequestDto requestDto, String email) {
        ChatRoom chatRoom = new ChatRoom(requestDto);
        System.out.println("=============================1");
        chatRoomRepository.save(chatRoom);
        System.out.println("==============================2");
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        System.out.println("==============================3");
        Participant participant = new Participant(user, chatRoom);
        System.out.println("============================4");
        participantRepository.save(participant);
        System.out.println("=================================5");
        chatRoom.addParticipant(participant);
        System.out.println("==================================6");
        user.addParticipant(participant);
        System.out.println("============================7");
        ChatRoomResponseDto responseDto = new ChatRoomResponseDto(chatRoom);
        System.out.println("===========================13");
        return responseDto;
    }

    @Transactional
    public ChatRoomResponseDto createPublicRoom(ChatRoomPublicRequestDto requestDto, String email) {
        ChatRoom chatRoom = new ChatRoom(requestDto);
        System.out.println("=============================1");
        chatRoomRepository.save(chatRoom);
        System.out.println("==============================2");
        chatMessageService.addParticipant(email, chatRoom.getRoomId());
        System.out.println("==============================9");
        return new ChatRoomResponseDto(chatRoom);
    }

    @Transactional
    public List<ChatRoomResponseDto> findAllRoom() {
        List<ChatRoomResponseDto> responseDtoList = new ArrayList<>();
        List<ChatRoom> roomList = chatRoomRepository.findAll();
        for (ChatRoom room : roomList) {
            ChatRoomResponseDto responseDto = new ChatRoomResponseDto(room);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    public ChatRoomResponseDto findById(String id) {
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        return new ChatRoomResponseDto(room);
    }


}
