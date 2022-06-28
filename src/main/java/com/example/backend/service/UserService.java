package com.example.backend.service;

import com.example.backend.domain.EmailCheck;
import com.example.backend.domain.User;
import com.example.backend.dto.MsgEnum;
import com.example.backend.dto.RequestEmailCheckDto;
import com.example.backend.dto.RequestRegisterDto;
import com.example.backend.repository.EmailCheckRepository;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    final private JavaMailSender javaMailSender;
//    private final PasswordEncoder passwordEncoder;
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
        simpleMessage.setSubject(MsgEnum.emailTitle.getMsg());

        String code = ThreadLocalRandom.current().nextInt(100000, 1000000)+"";

        EmailCheck emailCheck = EmailCheck.builder()
                .email(email)
                .code(code)
                .build();

        emailCheckRepository.save(emailCheck);

        simpleMessage.setText(MsgEnum.emailContentFront.getMsg()+ code);

        javaMailSender.send(simpleMessage);

        return MsgEnum.emailSend.getMsg();
    }

    public String emailCertificationCheck(RequestEmailCheckDto emailCheckDto) {
        List<EmailCheck> emailCheck = getEmailCheckList(emailCheckDto);
        if (emailCheck.get(0).getCode().equals(emailCheckDto.getCode())){
            return MsgEnum.correctEmailCode.getMsg();
        }
        throw new IllegalArgumentException(MsgEnum.incorrectEmailCode.getMsg());

    }

    public String nickCheck(RequestRegisterDto registerDto) {
        //중복 닉네임 체크
        dupleNickCheck(registerDto.getNick());

        return MsgEnum.availableNick.getMsg();
    }

    public String register(RequestRegisterDto registerDto) {
        dupleEmailCheck(registerDto.getEmail());
        dupleNickCheck(registerDto.getNick());

        User user = User.builder()
                .email(registerDto.getEmail())
                .nick(registerDto.getNick())
                .password(registerDto.getPassword()) //passwordEncoder.encode()
                .build();

        userRepository.save(user);

        //인증 이메일 테이블 삭제하는 로직 적용할지 생각해보기

        return MsgEnum.registerSuccess.getMsg();
    }

    private void dupleEmailCheck(String email) {
        if(userRepository.findByEmail(email).isPresent()){
            throw new IllegalArgumentException(MsgEnum.dupleEmail.getMsg());
        }
    }

    private void dupleNickCheck(String nick) {
        if(userRepository.findByNick(nick).isPresent()){
            throw new IllegalArgumentException(MsgEnum.dupleNick.getMsg());
        }
    }

    private List<EmailCheck> getEmailCheckList(RequestEmailCheckDto emailCheckDto) {
        List<EmailCheck> emailCheck = emailCheckRepository.findByEmailOrderByCreatedDateDesc(emailCheckDto.getEmail());
        if (emailCheck.size() == 0){
            throw new IllegalArgumentException(MsgEnum.incorrectEmailCode.getMsg());
        }
        return emailCheck;
    }
}
