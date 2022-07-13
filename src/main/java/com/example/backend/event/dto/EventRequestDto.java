package com.example.backend.event.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@ApiModel(value = "이벤트 요청 객체", description = "이벤트 당첨 후 휴대폰을 등록 하기 위한 객체")
public class EventRequestDto {

    @ApiModelProperty(value="제목", example = "010-0000-0000", required = true)
    @NotBlank(message = "휴대폰 번호는 필수 입력 값 입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",
            message = "010-0000-0000 형식에 맞춰주세요")
    private String phone;

    @ApiModelProperty(value="당첨시 반환된 이벤트 ID", example = "300", required = true)
    @NotNull(message = "이벤트 ID는 필수 입력 값입니다.")
    private Long eventId;
}
