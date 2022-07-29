package com.example.backend.user.controller;

import com.example.backend.msg.MsgEnum;
import com.example.backend.user.common.LoadUser;
import com.example.backend.user.dto.NickRequestDto;
import com.example.backend.user.dto.PasswordRequestDto;
import com.example.backend.user.dto.SocialUserCheckResponseDto;
import com.example.backend.user.dto.UserResponseDto;
import com.example.backend.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @ApiOperation(value = "사용자 정보 조회")
    @GetMapping
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<UserResponseDto> getUser() {
        log.info(LoadUser.getEmail());
        return ResponseEntity
                .ok()
                .body(userService.getUserInfo(LoadUser.getEmail()));
    }

    @ApiOperation(value = "사용자 프로필 사진 변경")
    @PutMapping("/profile")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<UserResponseDto> updateProfile(@RequestPart(value = "file", required = false) MultipartFile file){
        LoadUser.loginAndNickCheck();
        return ResponseEntity
                .ok()
                .body(userService.updateProfile(file, LoadUser.getEmail()));
    }

    @ApiOperation(value = "사용자 닉네임 변경")
    @PutMapping("/nick")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<UserResponseDto> updateNick(@Valid @RequestBody NickRequestDto nickRequestDto){
        LoadUser.loginAndNickCheck();
        return ResponseEntity
                .ok()
                .body(userService.updateNick(nickRequestDto.getNick(), LoadUser.getEmail()));
    }

    @ApiOperation(value = "사용자 비밀번호 변경")
    @PutMapping("/password")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody PasswordRequestDto passwordRequestDto){
        LoadUser.loginAndNickCheck();
        userService.updatePassword(LoadUser.getEmail(), passwordRequestDto);
        return ResponseEntity
                .ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.PASSWORD_UPDATE_SUCCESS.getMsg());
    }

    @ApiOperation(value = "비밀번호 변경 전 소셜 회원가입 확인")
    @GetMapping("/social")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<SocialUserCheckResponseDto> checkSocialUser(){
        LoadUser.loginAndNickCheck();
        return ResponseEntity
                .ok()
                .body(userService.checkSocialUser(LoadUser.getEmail()));
    }

    @ApiOperation(value = "회원 탈퇴")
    @DeleteMapping
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<String> deleteUser(){
        LoadUser.loginAndNickCheck();
        userService.deleteUser(LoadUser.getEmail());
        return ResponseEntity
                .ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.DELETE_USER.getMsg());
    }

}
