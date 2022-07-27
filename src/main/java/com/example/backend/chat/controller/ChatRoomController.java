package com.example.backend.chat.controller;

import com.example.backend.chat.dto.request.ChatRoomEnterRequestDto;
import com.example.backend.chat.dto.request.ChatRoomExitRequestDto;
import com.example.backend.chat.dto.request.ChatRoomPrivateRequestDto;
import com.example.backend.chat.dto.request.ChatRoomPublicRequestDto;
import com.example.backend.chat.dto.response.ChatRoomResponseDto;
import com.example.backend.chat.service.ChatRoomService;
import com.example.backend.msg.MsgEnum;
import com.example.backend.user.common.LoadUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "일대일 채팅방 생성")
    @PostMapping("/room/private")
    public ResponseEntity<ChatRoomResponseDto> createPrivateRoom(@RequestBody ChatRoomPrivateRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        ChatRoomResponseDto responseDto = chatRoomService.createPrivateRoom(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "단체 채팅방 입장")
    @PostMapping("/room/enter/public")
    public ResponseEntity<String> enterPublicRoom(@RequestBody ChatRoomEnterRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        chatRoomService.enterPublicRoom(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.CHAT_ROOM_ENTER.getMsg());
    }

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "채팅방 목록 조회")
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomResponseDto>> getRoomList() {
        LoadUser.loginAndNickCheck();
        List<ChatRoomResponseDto> responseDtoList = chatRoomService.findAllRoom(LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "채팅방 상세 조회")
    @GetMapping("/room/{id}")
    public ResponseEntity<ChatRoomResponseDto> getRoom(@PathVariable String id) {
        LoadUser.loginAndNickCheck();
        ChatRoomResponseDto responseDto = chatRoomService.findById(id, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "채팅방 나가기")
    @DeleteMapping("/room")
    public ResponseEntity<String> exitRoom(@RequestBody ChatRoomExitRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        chatRoomService.exitRoom(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.CHAT_ROOM_EXIT.getMsg());
    }
}
