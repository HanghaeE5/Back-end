package com.example.backend.user.controller;

import com.example.backend.msg.MsgEnum;

import com.example.backend.user.domain.User;
import com.example.backend.user.dto.RegisterRequestDto;
import com.example.backend.user.dto.RequestLoginDto;
import com.example.backend.user.security.UserDetailsImpl;
import com.example.backend.user.service.KakaoUserService;
import com.example.backend.user.service.UserService;
import com.example.backend.validate.UserValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class LoginController {

    final private UserService userService;
    final private KakaoUserService kakaoUserService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody RequestLoginDto loginDto){
        return ResponseEntity.ok()
                .header(MsgEnum.JWT_HEADER_NAME.getMsg(), userService.login(loginDto))
                .body(MsgEnum.LOGIN_SUCCESS.getMsg());
    }

    @GetMapping("/login/kakao/callback")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response) throws IOException {
        // authorizedCode: 카카오 서버로부터 받은 인가 코드
        //jwt 토큰
        User user = kakaoUserService.kakaoLogin(code);
        String nickCheck = user.getNick() == null ? "&nick=N" : "&nick=Y";
        String token = kakaoUserService.forceLogin(user);

        //1. 닉네임이 입력 안됬을 경우, http://www.ohniga.com?Authorization=토큰값&nick=N
        //2. 회원가입 완료, http://www.ohniga.com?Authorization=토큰값&nick=Y

        response.sendRedirect(
        "http://localhost:3000?"
                +MsgEnum.jwtHeaderName.getMsg()
                +"="
                +token
                +nickCheck
        );
    }

    @PutMapping("/register/social")
    public ResponseEntity<String> socialRegister(
            @RequestBody RegisterRequestDto registerRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        UserValidation.loginCheck(userDetails);
        return ResponseEntity.ok()
                    .header(MsgEnum.jwtHeaderName.getMsg(), userService.addNick(userDetails.getUsername(),registerRequestDto.getNick()))
                    .body(MsgEnum.socialRegisterSuccess.getMsg());
    }

}
