package com.example.backend.user.controller;

import com.example.backend.msg.MsgEnum;
import com.example.backend.user.common.LoadUser;
import com.example.backend.user.dto.EmailCheckRequestDto;
import com.example.backend.user.dto.RegisterRequestDto;
import com.example.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    final UserService userService;

    @PostMapping("/register/email")
    public ResponseEntity<String> emailCertification(@RequestBody RegisterRequestDto registerDto){
        return ResponseEntity.ok(
                userService.emailCertification(registerDto.getEmail())
        );
    }

    @PostMapping("/register/email-check")
    public ResponseEntity<String> emailCertificationCheck(@RequestBody EmailCheckRequestDto emailCheckDto){
        return ResponseEntity.ok(
                userService.emailCertificationCheck(emailCheckDto)
        );
    }

    @PostMapping("/register/nick-check")
    public ResponseEntity<String> nickCheck(@RequestBody RegisterRequestDto registerDto){
        return ResponseEntity.ok(
                userService.nickCheck(registerDto)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto registerDto){
        return ResponseEntity.ok(
                userService.register(registerDto)
        );
    }

    @PutMapping("/register/social")
    public ResponseEntity<String> socialRegister(
            @RequestBody RegisterRequestDto registerRequestDto){
        LoadUser.loginCheck();
        Map<String, String> token = userService.addNick(LoadUser.getEmail(),registerRequestDto.getNick());

        return ResponseEntity.ok()
                    .header(MsgEnum.JWT_HEADER_NAME.getMsg(), token.get(MsgEnum.JWT_HEADER_NAME.getMsg()))
                    .header(MsgEnum.REFRESH_HEADER_NAME.getMsg(), token.get(MsgEnum.REFRESH_HEADER_NAME.getMsg()))
                    .body(MsgEnum.SOCIAL_REGISTER_SUCCESS.getMsg());
    }
}
