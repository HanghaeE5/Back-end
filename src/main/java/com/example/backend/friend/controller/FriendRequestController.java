package com.example.backend.friend.controller;

import com.example.backend.friend.dto.FriendRequestDto;
import com.example.backend.friend.service.FriendRequestService;
import com.example.backend.user.common.LoadUser;
import com.example.backend.user.dto.UserResponseDto;
import com.example.backend.user.dto.UserTodoResponseDto;
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
@RequiredArgsConstructor
@RequestMapping("/friend")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    // 요청 수락은 또 한번의 요청으로 생각 or 요청에 대한 수락 따로 구현
    @ApiOperation(value = "친구 요청")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @PostMapping("/request")
    public ResponseEntity<String> requestFriend(
            @RequestBody FriendRequestDto requestDto
    ) {
        LoadUser.loginAndNickCheck();
        String message = friendRequestService.requestFriend(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(message);
    }

    @ApiOperation(value = "친구 요청 수락")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @PostMapping("/accept")
    public ResponseEntity<String> acceptFriend(
            @RequestBody FriendRequestDto requestDto
    ) {
        LoadUser.loginAndNickCheck();
        friendRequestService.accept(LoadUser.getEmail(), requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body("요청을 수락했습니다");
    }

    @ApiOperation(value = "친구 목록 조회")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDto>> getList() {
        LoadUser.loginAndNickCheck();
        List<UserResponseDto> responseDtoList = friendRequestService.getFriendList(LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @ApiOperation(value = "친구 요청 목록 조회")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @GetMapping("/request/list")
    public ResponseEntity<List<UserResponseDto>> getRequestList() {
        LoadUser.loginAndNickCheck();
        List<UserResponseDto> responseDtoList = friendRequestService.getRequestList(LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }

    @ApiOperation(value = "친구 페이지 조회")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @GetMapping("/page/{nick}")
    public ResponseEntity<UserTodoResponseDto> getFriendPage(
            @PathVariable String nick
    ) {
        LoadUser.loginAndNickCheck();
        UserTodoResponseDto responseDto = friendRequestService.getFriendPage(LoadUser.getEmail(), nick);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @ApiOperation(value = "친구 요청 거절")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @DeleteMapping("/reject")
    public ResponseEntity<String> rejectFriend(@RequestBody FriendRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        friendRequestService.reject(LoadUser.getEmail(), requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body("요청을 거절했습니다");
    }

    @ApiOperation(value = "친구 삭제")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteFriend(@RequestBody FriendRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        friendRequestService.delete(LoadUser.getEmail(), requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body("친구를 삭제했습니다");
    }

    @ApiOperation(value = "친구 요청 취소")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @DeleteMapping("/cancel")
    public ResponseEntity<String> cancelRequest(@RequestBody FriendRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        friendRequestService.cancel(LoadUser.getEmail(), requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body("친구 요청을 취소했습니다");
    }
}
