package com.example.backend.user.service;

import com.example.backend.user.domain.User;
import com.example.backend.user.dto.KakaoUserInfoDto;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.security.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoUserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public User kakaoLogin(String code) throws JsonProcessingException {
        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        RestTemplate rt = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code, headers, rt, objectMapper);

        // 2. 토큰으로 카카오 API 호출
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken, headers, rt, objectMapper);

        //3. 필요시에 회원가입 / 회원가입 되어있다면 사용자 정보
        return registerKakaoUserIfNeeded(kakaoUserInfo);
    }

    private String getAccessToken(String code, HttpHeaders headers, RestTemplate rt, ObjectMapper objectMapper) throws JsonProcessingException {
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "6a828e9aabac1f17a3e5c7b9ddab9209");
        body.add("redirect_uri", "http://localhost:8080/login/kakao/callback");
        body.add("code", code);

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken, HttpHeaders headers, RestTemplate rt, ObjectMapper objectMapper) throws JsonProcessingException {
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String>  response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("kakao_account").get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + email);
        return new KakaoUserInfoDto(id, email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);
        if (kakaoUser == null) {
            //이메일로 회원가입한 사용자 카카오 로그인 가능하게 변경
            User basicUser = userRepository.findByEmail(kakaoUserInfo.getEmail()).orElse(null);
            if(basicUser != null) {
                basicUser.userToKakaoUser(kakaoId);
                kakaoUser = basicUser;
            }else{
                // 회원가입

                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();
                // role: 일반 사용자
                List<String> role = Collections.singletonList(Role.USER.getName());

                kakaoUser = new User(encodedPassword, email, role, kakaoId);
                userRepository.save(kakaoUser);
            }

        }
        return kakaoUser;
    }

    public String forceLogin(User kakaoUser) {
        return jwtTokenProvider.createAccessToken(kakaoUser);
    }
}
