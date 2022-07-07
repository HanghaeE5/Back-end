package com.example.backend.friend.controller;

import com.example.backend.friend.dto.FriendRequestDto;
import com.example.backend.friend.service.FriendRequestService;
import com.example.backend.user.common.LoadUser;
import com.example.backend.user.dto.UserResponseDto;
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
    @PostMapping("/request")
    public ResponseEntity<String> requestFriend(@RequestBody FriendRequestDto requestDto) {
        LoadUser.loginAndNickCheck();
        String message = friendRequestService.requestFriend(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(message);
    }

    @PostMapping("/accept/{email}")
    public ResponseEntity<String> acceptFriend(@PathVariable String email) {
        LoadUser.loginAndNickCheck();
        friendRequestService.accept(LoadUser.getEmail(), email);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body("요청을 수락했습니다");
    }

    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDto>> getList() {
        LoadUser.loginAndNickCheck();
        List<UserResponseDto> userResponseDtoList = friendRequestService.getList(LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDtoList);
    }

    @DeleteMapping("/reject/{email}")
    public ResponseEntity<String> rejectFriend(@PathVariable String email) {
        LoadUser.loginAndNickCheck();
        friendRequestService.reject(LoadUser.getEmail(), email);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body("요청을 거절했습니다");
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteFriend(@PathVariable String email) {
        LoadUser.loginAndNickCheck();
        friendRequestService.delete(LoadUser.getEmail(), email);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body("친구를 삭제했습니다");
    }
}
