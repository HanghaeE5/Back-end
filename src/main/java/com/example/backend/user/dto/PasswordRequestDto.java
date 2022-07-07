package com.example.backend.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApiModel(value = "비밀번호 객체", description = "비밀번호를 받기위한 객체")
@Getter
public class PasswordRequestDto {

    @ApiModelProperty(value="비밀번호", example = "Abcd123", required = true)
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z]).{5,20}",
            message = "비밀번호는 5~20자 영문, 숫자 사용하세요.")
    private String password;
}
