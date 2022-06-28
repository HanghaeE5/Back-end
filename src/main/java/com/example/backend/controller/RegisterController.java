package com.example.backend.controller;

import com.example.backend.dto.RequestEmailCheckDto;
import com.example.backend.dto.RequestRegisterDto;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    final UserService userService;

    @PostMapping("/register/email")
    public ResponseEntity<String> emailCertification(@RequestBody RequestRegisterDto registerDto){
        return ResponseEntity.ok(
                userService.emailCertification(registerDto.getEmail())
        );
    }

    @PostMapping("/register/email-check")
    public ResponseEntity<String> emailCertificationCheck(@RequestBody RequestEmailCheckDto emailCheckDto){
        return ResponseEntity.ok(
                userService.emailCertificationCheck(emailCheckDto)
        );
    }

    @PostMapping("/register/nick-check")
    public ResponseEntity<String> nickCheck(@RequestBody RequestRegisterDto registerDto){
        return ResponseEntity.ok(
                userService.nickCheck(registerDto)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RequestRegisterDto registerDto){
        return ResponseEntity.ok(
                userService.register(registerDto)
        );
    }
}
