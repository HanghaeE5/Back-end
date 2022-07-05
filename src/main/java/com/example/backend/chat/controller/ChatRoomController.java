package com.example.backend.chat.controller;

import com.example.backend.chat.dto.ChatRoomRequestDto;
import com.example.backend.chat.dto.ChatRoomResponseDto;
import com.example.backend.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/room")
    public ResponseEntity<ChatRoomResponseDto> createRoom(@RequestBody ChatRoomRequestDto requestDto) {
        ChatRoomResponseDto responseDto = chatRoomService.createRoom(requestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getRoomList() {
        List<ChatRoomResponseDto> responseDtoList = chatRoomService.findAllRoom();
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
}
