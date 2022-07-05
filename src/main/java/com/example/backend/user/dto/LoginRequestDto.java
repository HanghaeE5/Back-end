package com.example.backend.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "로그인 객체", description = "로그인을 하기 위한 객체")
@Getter
public class LoginRequestDto {

    @ApiModelProperty(value="이메일", example = "example@naver.com", required = true)
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @ApiModelProperty(value="비밀번호", example = "Abcd123!", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
