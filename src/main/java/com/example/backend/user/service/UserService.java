package com.example.backend.user.service;

import com.example.backend.board.domain.Board;
import com.example.backend.board.repository.BoardRepository;
import com.example.backend.character.dto.CharacterResponseDto;
import com.example.backend.character.service.CharacterService;
import com.example.backend.event.domain.Stamp;
import com.example.backend.event.repository.StampRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.mail.EmailService;
import com.example.backend.msg.MsgEnum;
import com.example.backend.notification.service.EmitterService;
import com.example.backend.s3.AwsS3Service;
import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.dto.response.TodoResponseDto;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.user.config.AppProperties;
import com.example.backend.user.domain.*;
import com.example.backend.user.dto.*;
import com.example.backend.user.repository.EmailCheckRepository;
import com.example.backend.user.repository.UserRefreshTokenRepository;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.token.AuthToken;
import com.example.backend.user.token.AuthTokenProvider;
import com.example.backend.utils.CookieUtil;
import com.example.backend.utils.HeaderUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final CharacterService characterService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final EmailCheckRepository emailCheckRepository;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final UserRefreshTokenRepository userRefreshTokenRepository;
    private final StampRepository stampRepository;
    private final static long THREE_DAYS_MSEC = 259200000;
    private final AwsS3Service awsS3Service;
    private final TodoRepository todoRepository;
    private final EmailService emailService;
    private final BoardRepository boardRepository;
    private final EmitterService emitterService;

    @Value("${basic.profile.img}")
    private String basicImg;

    public String emailCertification(String email) {

        //중복이메일 체크
        dupleEmailCheck(email);
        String code = ThreadLocalRandom.current().nextInt(100000, 1000000)+"";
        emailCheckRepository.save(new EmailCheck(email, code));

        emailService.sendEmail(email, code);

        return MsgEnum.EMAIL_SEND.getMsg();
    }

    @Transactional
    public String emailCertificationCheck(EmailCheckRequestDto emailCheckDto) {
        List<EmailCheck> emailCheck = getEmailCheckList(emailCheckDto.getEmail());
        EmailCheck firstValue = emailCheck.get(0);
        if (firstValue.getCode().equals(emailCheckDto.getCode())){
            //인증 완료시 Y로 바꾸기
            firstValue.verificationCompleted("Y");
            return MsgEnum.CORRECT_EMAIL_CODE.getMsg();
        }
        throw new CustomException(ErrorCode.INCORRECT_EMAIL_CODE);
    }

    public String nickCheck(NickRequestDto registerDto) {
        //중복 닉네임 체크
        dupleNickCheck(registerDto.getNick());

        return MsgEnum.AVAILABLE_NICK.getMsg();
    }

    @Transactional
    public String register(RegisterRequestDto registerDto) {
        //이메일 중복
        dupleEmailCheck(registerDto.getEmail());
        //닉네임 중복
        dupleNickCheck(registerDto.getNick());
        //인증 메일 보냈나 확인

        List<EmailCheck> emailChecks = getEmailCheckList(registerDto.getEmail());
        if (emailChecks.get(0).getConfirmYn() == null){
            throw new CustomException(ErrorCode.INCORRECT_EMAIL_CODE);
        }
        if (emailChecks.get(0).getConfirmYn().equals("N")){
            throw new CustomException(ErrorCode.INCORRECT_EMAIL_CODE);
        }

        User user = User.builder()
                .email(registerDto.getEmail())
                .username(registerDto.getNick())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roleType(RoleType.USER)
                .providerType(ProviderType.LOCAL)
                .profileImageUrl(basicImg)
                .build();

        User saveUser = userRepository.save(user);
        stampRepository.save(new Stamp(saveUser));

        //인증한 이메일 삭제
        emailCheckRepository.deleteByEmail(user.getEmail());

        return MsgEnum.REGISTER_SUCCESS.getMsg();
    }

    private void dupleEmailCheck(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new CustomException(ErrorCode.DUPLE_EMAIL);
        }
    }

    private void dupleNickCheck(String nick) {
        if(userRepository.findByUsername(nick).isPresent()){
            throw new CustomException(ErrorCode.DUPLE_NICK);
        }
    }

    private List<EmailCheck> getEmailCheckList(String email) {
        List<EmailCheck> emailCheck = emailCheckRepository.findByEmailOrderByCreatedDateDesc(email);
        if (emailCheck.size() == 0){
            throw new CustomException(ErrorCode.INCORRECT_EMAIL_CODE);
        }
        return emailCheck;
    }

    @Transactional
    public String login(LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        //회원 있는지 없는지 체크
        User user = userRepository.findByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.CONFIRM_EMAIL_PWD));

        //비밀번호 체크
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.CONFIRM_EMAIL_PWD);
        }

        //create accessToken
        Date now = new Date();
        AuthToken accessToken = tokenProvider.createAuthToken(
                user.getEmail(),
                RoleType.USER.getCode(),
                user.getUsername(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        //create refreshToken
        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        // userId refresh token 으로 DB 확인
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByEmail(user.getEmail());
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new UserRefreshToken(user.getEmail(), refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

//        int cookieMaxAge = (int) refreshTokenExpiry / 60;
//        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
//        CookieUtil.addCookie(response, REFRESH_TOKEN, userRefreshToken.getRefreshToken(), cookieMaxAge);

        return accessToken.getToken();
    }

    @Transactional
    public String refresh(HttpServletRequest request, HttpServletResponse response){
        String accessToken = HeaderUtil.getAccessToken(request);
        AuthToken authToken = tokenProvider.convertAuthToken(accessToken);

        // 만료된 access token 인지 확인
        Claims claims = authToken.getExpiredTokenClaims();
        if (claims == null) {
            throw new CustomException(ErrorCode.NOT_EXPIRED_TOKEN_YET);
        }

        String email = claims.getSubject();
        String username = claims.get("nick", String.class);
        RoleType roleType = RoleType.of(claims.get("role", String.class));

        // refresh token
        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByEmail(email);
        if (userRefreshToken == null){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        AuthToken authRefreshToken = tokenProvider.convertAuthToken(userRefreshToken.getRefreshToken());
        if (!authRefreshToken.validate()) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        //새로운 AccessToken 생성
        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                email,
                roleType.getCode(),
                username,
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );
        // refresh 토큰 기간이 3일 이하로 남은 경우, refresh 토큰 갱신
        long validTime = authRefreshToken.getTokenClaims().getExpiration().getTime() - now.getTime();
        if (validTime <= THREE_DAYS_MSEC) {
            // refresh 토큰 설정
            long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

            authRefreshToken = tokenProvider.createAuthToken(
                    appProperties.getAuth().getTokenSecret(),
                    new Date(now.getTime() + refreshTokenExpiry)
            );
            // DB에 refresh 토큰 업데이트 해주기
            userRefreshToken.setRefreshToken(authRefreshToken.getToken());

//            int cookieMaxAge = (int) refreshTokenExpiry / 60;
//            CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
//            CookieUtil.addCookie(response, REFRESH_TOKEN, authRefreshToken.getToken(), cookieMaxAge);

        }

        return newAccessToken.getToken();
    }

    @Transactional
    public String addNick(String email, String nick, HttpServletRequest request, HttpServletResponse response) {

        User user = getUser(email);
        dupleNickCheck(nick);
        user.addNick(nick);

        Date now = new Date();
        AuthToken newAccessToken = tokenProvider.createAuthToken(
                email,
                user.getRoleType().getCode(),
                user.getUsername(),
                new Date(now.getTime() + appProperties.getAuth().getTokenExpiry())
        );

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();
        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        UserRefreshToken userRefreshToken = userRefreshTokenRepository.findByEmail(email);
        if (userRefreshToken == null) {
            // 없는 경우 새로 등록
            userRefreshToken = new UserRefreshToken(email, refreshToken.getToken());
            userRefreshTokenRepository.saveAndFlush(userRefreshToken);
        } else {
            // DB에 refresh 토큰 업데이트
            userRefreshToken.setRefreshToken(refreshToken.getToken());
        }

//        int cookieMaxAge = (int) refreshTokenExpiry / 60;
//        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
//        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        return newAccessToken.getToken();
    }

    public UserResponseDto getUserInfo(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        if (user.getUsername() == null){
            throw new CustomException(ErrorCode.NEED_NICK);
        }
        CharacterResponseDto characterResponseDto = characterService.getCharacterInfo(email);

        List<Todo> todoList = todoRepository.findAllByTodoDate(user);
        List<TodoResponseDto> responseDtoList = new ArrayList<>();
        for (Todo t : todoList) {
            responseDtoList.add(new TodoResponseDto(t));
        }
        return new UserResponseDto(getUser(email), characterResponseDto, responseDtoList);
    }

    @Transactional
    public UserResponseDto updateProfile(MultipartFile file, String email) {
        User user = getUser(email);

//        if (!basicImg.equals(user.getProfileImageUrl())){
//            awsS3Service.deleteImage(user.getProfileImageUrl().split(MsgEnum.IMAGE_DOMAIN.getMsg())[1]);
//        }

        String imgUrl = basicImg;
        if (file != null){
            imgUrl = awsS3Service.uploadImage(file);
        }

        user.updateProfileImage(imgUrl);

        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateNick(String nick, String email) {
        User user = getUser(email);
        dupleNickCheck(nick);

        user.updateNick(nick);

        return new UserResponseDto(user);
    }

    @Transactional
    public void updatePassword(String email, PasswordRequestDto passwordRequestDto) {
        User user = getUser(email);
        //소셜 회원이 비밀번호를 변경하는지 체크
        if (user.getPassword().equals("NO_PASS")){
            throw new CustomException(ErrorCode.SOCIAL_NOT_UPDATE_PASSWORD);
        }

        //기존 비밀번호 체크
        if (!passwordEncoder.matches(passwordRequestDto.getOldPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_OLD_PWD);
        }

        user.updatePassword(passwordEncoder.encode(passwordRequestDto.getNewPassword()));
    }

    @Transactional
    public void deleteUser(String email){
        User user = getUser(email);

        Long count = todoRepository.updateTodoByBoardIn(boardRepository.findByUser(user));

        userRepository.delete(user);
    }


    public SocialUserCheckResponseDto checkSocialUser(String email) {
        User user = getUser(email);
        String msg = "";
        boolean socialUser = true;
        if (user.getPassword().equals("NO_PASS")){
            msg = "소셜 회원 입니다.";
        }else{
            msg = "TodoWith 회원 입니다.";
            socialUser = false;
        }

        return new SocialUserCheckResponseDto(msg, socialUser);
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }
}
