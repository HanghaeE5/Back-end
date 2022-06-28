package com.example.backend.controller;

import com.example.backend.dto.MsgEnum;
import com.example.backend.dto.RequestLoginDto;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    final private UserService userService;
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RequestLoginDto loginDto){
        return ResponseEntity.ok()
                .header(MsgEnum.jwtHeaderName.getMsg(), userService.login(loginDto))
                .body(MsgEnum.loginSuccess.getMsg());
    }
}
