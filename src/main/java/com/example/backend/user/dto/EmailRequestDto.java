package com.example.backend.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "이메일 객체", description = "이메일 인증을 요청 위한 객체 정보")
@Getter
public class EmailRequestDto {

    @ApiModelProperty(value="이메일", example = "example@naver.com", required = true)
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;
}
