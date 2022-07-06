package com.example.backend.chat.service;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.dto.ChatRoomRequestDto;
import com.example.backend.chat.dto.ChatRoomResponseDto;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoomResponseDto createRoom(ChatRoomRequestDto requestDto) {
        ChatRoom room = new ChatRoom(requestDto);
        chatRoomRepository.save(room);
        return new ChatRoomResponseDto(room);
    }

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
