package com.example.backend.user.service;

import com.example.backend.msg.MsgEnum;
import com.example.backend.user.domain.EmailCheck;
import com.example.backend.user.domain.User;
import com.example.backend.exception.ErrorCode;

import com.example.backend.user.dto.EmailCheckRequestDto;
import com.example.backend.user.dto.RegisterRequestDto;
import com.example.backend.user.dto.RequestLoginDto;
import com.example.backend.user.repository.EmailCheckRepository;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    final private JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    final private UserRepository userRepository;
    final private EmailCheckRepository emailCheckRepository;
    @Value("${spring.mail.username}")
    private String adminMail;

    public String emailCertification(String email) {

        //중복이메일 체크
        dupleEmailCheck(email);

        //SMTP 이메일 전송 셋팅 / 인증번호 생성
        SimpleMailMessage simpleMessage = new SimpleMailMessage();
        simpleMessage.setFrom(adminMail);
        simpleMessage.setTo(email);
        simpleMessage.setSubject(MsgEnum.EMAIL_TITLE.getMsg());

        String code = ThreadLocalRandom.current().nextInt(100000, 1000000)+"";

        EmailCheck emailCheck = EmailCheck.builder()
                .email(email)
                .code(code)
                .build();

        emailCheckRepository.save(emailCheck);

        simpleMessage.setText(MsgEnum.EMAIL_CONTENT_FRONT.getMsg()+ code);

        javaMailSender.send(simpleMessage);

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

        throw new IllegalArgumentException(ErrorCode.INCORRECT_EMAIL_CODE.getMsg());

    }

    public String nickCheck(RegisterRequestDto registerDto) {
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
        if (emailChecks.get(0).getConfirmYn().equals("N")){
            throw new IllegalArgumentException(ErrorCode.INCORRECT_EMAIL_CODE.getMsg());
        }

        User user = User.builder()
                .email(registerDto.getEmail())
                .nick(registerDto.getNick())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(Collections.singletonList(Role.USER.getName()))
                .build();

        userRepository.save(user);

        //인증한 이메일 삭제
        emailCheckRepository.deleteByEmail(user.getEmail());

        return MsgEnum.REGISTER_SUCCESS.getMsg();
    }

    private void dupleEmailCheck(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException(ErrorCode.DUPLE_EMAIL.getMsg());
        }
    }

    private void dupleNickCheck(String nick) {
        if(userRepository.findByNick(nick).isPresent()){
            throw new IllegalArgumentException(ErrorCode.DUPLE_NICK.getMsg());
        }
    }

    private List<EmailCheck> getEmailCheckList(String email) {
        List<EmailCheck> emailCheck = emailCheckRepository.findByEmailOrderByCreatedDateDesc(email);
        if (emailCheck.size() == 0){
            throw new IllegalArgumentException(ErrorCode.INCORRECT_EMAIL_CODE.getMsg());
        }
        return emailCheck;
    }

    public String login(RequestLoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.CONFIRM_EMAIL_PWD.getMsg()));

        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(ErrorCode.CONFIRM_EMAIL_PWD.getMsg());
        }
        return jwtTokenProvider.createAccessToken(user);
    }

    @Transactional
    public String addNick(String email, String nick) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ErrorCode.USER_NOT_FOUND.getMsg()));

        dupleNickCheck(nick);
        user.addNick(nick);

        return jwtTokenProvider.createAccessToken(user);
    }

    public User getUser(String userId) {
        return userRepository.findByUserId(userId);
    }
}
