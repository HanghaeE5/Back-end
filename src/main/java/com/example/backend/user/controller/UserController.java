package com.example.backend.user.controller;

import com.example.backend.user.common.LoadUser;
import com.example.backend.user.dto.NickRequestDto;
import com.example.backend.user.dto.UserResponseDto;
import com.example.backend.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "사용자 정보 조회")
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser() {
        return ResponseEntity
                .ok()
                .body(userService.getUserInfo(LoadUser.getEmail()));
    }

    @ApiOperation(value = "사용자 프로필 사진 변경")
    @PutMapping("/profile")
    public ResponseEntity<UserResponseDto> updateProfile(@RequestPart(value = "file") MultipartFile file){
        LoadUser.loginAndNickCheck();
        return ResponseEntity
                .ok()
                .body(userService.updateProfile(file, LoadUser.getEmail()));
    }

    @ApiOperation(value = "사용자 닉네임 변경")
    @PutMapping("/nick")
    public ResponseEntity<UserResponseDto> updateNick(@Valid @RequestBody NickRequestDto nickRequestDto){
        LoadUser.loginAndNickCheck();
        return ResponseEntity
                .ok()
                .body(userService.updateNick(nickRequestDto.getNick(), LoadUser.getEmail()));
    }
}
