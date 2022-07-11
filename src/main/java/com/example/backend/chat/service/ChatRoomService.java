package com.example.backend.chat.service;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.Participant;
import com.example.backend.chat.dto.request.ChatRoomPrivateRequestDto;
import com.example.backend.chat.dto.request.ChatRoomPublicRequestDto;
import com.example.backend.chat.dto.response.ChatRoomResponseDto;
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
    @Transactional
    public ChatRoomResponseDto createPrivateRoom(ChatRoomPrivateRequestDto requestDto, String email) {
        ChatRoom chatRoom = new ChatRoom(requestDto);
        chatRoomRepository.save(chatRoom);
        // 나와 친구 둘다 채팅방에 참가자로 추가
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User friend = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom room = chatRoomRepository.findById(chatRoom.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        Participant participantMe = new Participant(user, room);
        Participant participantYou = new Participant(friend, room);
        participantRepository.save(participantMe);
        participantRepository.save(participantYou);
        room.addParticipant(participantMe);
        room.addParticipant(participantYou);
        user.addParticipant(participantMe);
        friend.addParticipant(participantYou);
        return new ChatRoomResponseDto(room);
    }

    // 단체 톡방
    @Transactional
    public ChatRoomResponseDto createPublicRoom(ChatRoomPublicRequestDto requestDto, String email) {
        ChatRoom room = new ChatRoom(requestDto);
        chatRoomRepository.save(room);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom chatRoom = chatRoomRepository.findById(room.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        Participant participant = new Participant(user, chatRoom);
        participantRepository.save(participant);
        chatRoom.addParticipant(participant);
        user.addParticipant(participant);
        return new ChatRoomResponseDto(chatRoom);
    }

    @Transactional
    public List<ChatRoomResponseDto> findAllRoom(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        List<ChatRoomResponseDto> responseDtoList = new ArrayList<>();
        List<Participant> participantList = participantRepository.findAllByUser(user);
        for (Participant p : participantList) {
            ChatRoom room = chatRoomRepository.findById(p.getChatRoom().getRoomId()).orElseThrow(
                    () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
            );
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
