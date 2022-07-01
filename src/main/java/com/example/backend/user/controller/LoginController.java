package com.example.backend.user.controller;

import com.example.backend.msg.MsgEnum;

import com.example.backend.user.common.ApiResponse;
import com.example.backend.user.config.AppProperties;
import com.example.backend.user.domain.AuthReqModel;
import com.example.backend.user.domain.User;
import com.example.backend.user.domain.UserRefreshToken;
import com.example.backend.user.dto.RegisterRequestDto;
import com.example.backend.user.dto.RequestLoginDto;
import com.example.backend.user.oauth.token.AuthToken;
import com.example.backend.user.oauth.token.AuthTokenProvider;
import com.example.backend.user.repository.UserRefreshTokenRepository;
import com.example.backend.user.security.UserDetailsImpl;
import com.example.backend.user.service.KakaoUserService;
import com.example.backend.user.service.UserService;
import com.example.backend.validate.UserValidation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    final private UserService userService;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    @PostMapping("/login")
    public ApiResponse login(@RequestBody AuthReqModel authReqModel) {
        log.info("/login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authReqModel.getEmail(),
                        authReqModel.getPassword()
                )
        );

        String email = authReqModel.getEmail();
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                email,
                ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByUserId(email);
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new UserRefreshToken(email, refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
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
                +MsgEnum.JWT_HEADER_NAME.getMsg()
                +"="
                +token
                +nickCheck
        );
    }

    @GetMapping("/login/oauth2/code/")
    public void googleLogin(@RequestParam String code, HttpServletResponse response){

    }


    @PutMapping("/register/social")
    public ResponseEntity<String> socialRegister(
            @RequestBody RegisterRequestDto registerRequestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        UserValidation.loginCheck(userDetails);
        return ResponseEntity.ok()
                    .header(MsgEnum.JWT_HEADER_NAME.getMsg(), userService.addNick(userDetails.getUsername(),registerRequestDto.getNick()))
                    .body(MsgEnum.SOCIAL_REGISTER_SUCCESS.getMsg());
    }

}
