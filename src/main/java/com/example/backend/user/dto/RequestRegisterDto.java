package com.example.backend.user.dto;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class RequestRegisterDto {

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{5,10}",
            message = "비밀번호는 5~20자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
    private String password;


    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z-0-9]{2,15}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nick;

}
