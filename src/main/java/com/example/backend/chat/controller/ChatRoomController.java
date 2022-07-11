package com.example.backend.chat.controller;

import com.example.backend.chat.dto.request.ChatRoomPrivateRequestDto;
import com.example.backend.chat.dto.request.ChatRoomPublicRequestDto;
import com.example.backend.chat.dto.response.ChatRoomResponseDto;
import com.example.backend.chat.service.ChatRoomService;
import com.example.backend.user.common.LoadUser;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "일대일 채팅방 생성")
    @PostMapping("/room/private")
    public ResponseEntity<ChatRoomResponseDto> createPrivateRoom(@RequestBody ChatRoomPrivateRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        ChatRoomResponseDto responseDto = chatRoomService.createPrivateRoom(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @ApiOperation(value = "단체 채팅방 생성")
    @PostMapping("/room/public")
    public ResponseEntity<ChatRoomResponseDto> createPublicRoom(@RequestBody ChatRoomPublicRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        ChatRoomResponseDto responseDto = chatRoomService.createPublicRoom(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @ApiOperation(value = "채팅방 목록 조회")
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getRoomList() {
        LoadUser.loginAndNickCheck();
        List<ChatRoomResponseDto> responseDtoList = chatRoomService.findAllRoom(LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @ApiOperation(value = "채팅방 상세 조회")
    @GetMapping("/room/{id}")
    public ResponseEntity<ChatRoomResponseDto> getRoom(@PathVariable String id) {
        LoadUser.loginAndNickCheck();
        ChatRoomResponseDto responseDto = chatRoomService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
