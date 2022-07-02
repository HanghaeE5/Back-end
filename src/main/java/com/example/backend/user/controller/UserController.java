package com.example.backend.user.controller;

import com.example.backend.user.common.LoadUser;
import com.example.backend.user.dto.UserResponseDto;
import com.example.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<UserResponseDto> getUser() {
        return ResponseEntity
                .ok()
                .body(userService.getUser(LoadUser.getEmail()));
    }
}
