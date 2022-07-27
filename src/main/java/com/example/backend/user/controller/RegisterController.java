package com.example.backend.user.controller;

import com.example.backend.msg.MsgEnum;
import com.example.backend.user.common.LoadUser;
import com.example.backend.user.dto.EmailCheckRequestDto;
import com.example.backend.user.dto.EmailRequestDto;
import com.example.backend.user.dto.NickRequestDto;
import com.example.backend.user.dto.RegisterRequestDto;
import com.example.backend.user.service.UserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RegisterController {

    final UserService userService;

    @ApiOperation(value = "이메일 인증하기")
    @PostMapping("/register/email")
    public ResponseEntity<String> emailCertification(@Valid @RequestBody EmailRequestDto emailRequestDto) throws MessagingException {
        return ResponseEntity.ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(userService.emailCertification(emailRequestDto.getEmail()));
    }

    @ApiOperation(value = "이메일 인증 확인")
    @PostMapping("/register/email-check")
    public ResponseEntity<String> emailCertificationCheck(@Valid @RequestBody EmailCheckRequestDto emailCheckDto){
        return ResponseEntity.ok()
            .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
            .body(userService.emailCertificationCheck(emailCheckDto));
    }


    @ApiOperation(value = "닉네임 중복 체크")
    @PostMapping("/register/nick-check")
    public ResponseEntity<String> nickCheck(@Valid @RequestBody NickRequestDto registerDto){
        return ResponseEntity.ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(userService.nickCheck(registerDto));
    }

    @ApiOperation(value = "로컬 회원가입")
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequestDto registerDto){
        return ResponseEntity.ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(userService.register(registerDto));
    }

    @ApiOperation(value = "소셜 회원가입 - 닉네임 입력")
    @PutMapping("/register/social")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    public ResponseEntity<String> socialRegister(@Valid @RequestBody NickRequestDto registerDto, HttpServletRequest request, HttpServletResponse response){
        LoadUser.loginCheck();

        return ResponseEntity.ok()
                    .header(MsgEnum.JWT_HEADER_NAME.getMsg(), userService.addNick(LoadUser.getEmail(),registerDto.getNick(), request, response))
                    .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                    .body(MsgEnum.SOCIAL_REGISTER_SUCCESS.getMsg());
    }
}
