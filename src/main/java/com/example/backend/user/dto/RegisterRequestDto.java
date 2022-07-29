package com.example.backend.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "회원가입 객체", description = "회원가입을 하기 위한 객체")
@Getter
public class RegisterRequestDto {

    @ApiModelProperty(value="이메일", example = "example@naver.com", required = true)
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @ApiModelProperty(value="비밀번호", example = "Abcd123", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{5,20}",
            message = "비밀번호는 5~20자 영문, 숫자 사용하세요.")
    private String password;


    @ApiModelProperty(value="닉네임", example = "hello", required = true)
    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z-0-9]{2,8}$", message = "닉네임은 특수문자를 제외한 2~8자리여야 합니다.")
    private String nick;

}
