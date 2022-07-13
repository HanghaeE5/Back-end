package com.example.backend.chat.service;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.Participant;
import com.example.backend.chat.domain.Type;
import com.example.backend.chat.dto.request.ChatRoomEnterRequestDto;
import com.example.backend.chat.dto.request.ChatRoomExitRequestDto;
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
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    // 일대일 채팅은 일단 보류
    @Transactional
    public ChatRoomResponseDto createPrivateRoom(ChatRoomPrivateRequestDto requestDto, String email) {

        // 나와 친구 둘다 채팅방에 참가자로 추가
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        User friend = userRepository.findByUsername(requestDto.getNick()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        // 이미 채팅방 있는지 확인
        // 이게 맞나......ㅋㅋ....
        List<ChatRoom> roomList = new ArrayList<>();
        List<Participant> participantList = participantRepository.findAllByUser(user);
        for (Participant p : participantList) {
            roomList.add(p.getChatRoom());
        }
        for (ChatRoom r : roomList) {
            if (r.getType() == Type.PRIVATE) {
                for (Participant p2 : r.getParticipantList()) {
                    if (p2.getUser() == friend) {
                        throw new CustomException(ErrorCode.EXISTING_ROOM);
                    }
                }
            }
        }
        // 채팅방 생성
        ChatRoom room = chatRoomRepository.save(new ChatRoom(requestDto));
        Participant participantMe = new Participant(user, room);
        Participant participantYou = new Participant(friend, room);
        participantRepository.save(participantMe);
        participantRepository.save(participantYou);
        room.addParticipant(participantMe);
        room.addParticipant(participantYou);
        user.addParticipant(participantMe);
        friend.addParticipant(participantYou);
        return new ChatRoomResponseDto(room, user);
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
        return new ChatRoomResponseDto(chatRoom, user);
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
            ChatRoomResponseDto responseDto = new ChatRoomResponseDto(room, user);
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    @Transactional
    public ChatRoomResponseDto findById(String id, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom room = chatRoomRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        return new ChatRoomResponseDto(room, user);
    }

    @Transactional
    public void exitRoom(ChatRoomExitRequestDto requestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom room = chatRoomRepository.findById(requestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        List<Participant> participantList = room.getParticipantList();
        for (Participant p : participantList) {
            if (p.getUser() == user) {
                participantRepository.delete(p);
                return;
            }
        }
    }

    @Transactional
    public void enterPublicRoom(ChatRoomEnterRequestDto requestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        ChatRoom room = chatRoomRepository.findById(requestDto.getRoomId()).orElseThrow(
                () -> new CustomException(ErrorCode.ROOM_NOT_FOUND)
        );
        participantRepository.save(new Participant(user, room));
    }
}
