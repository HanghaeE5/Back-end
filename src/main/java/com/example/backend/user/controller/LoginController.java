package com.example.backend.user.controller;

import com.example.backend.exception.ErrorCode;
import com.example.backend.exception.MsgEnum;
import com.example.backend.user.dto.RequestLoginDto;
import com.example.backend.user.service.UserService;
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
                .header(MsgEnum.JWT_HEADER_NAME.getMsg(), userService.login(loginDto))
                .body(MsgEnum.LOGIN_SUCCESS.getMsg());
    }
}
