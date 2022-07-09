package com.example.backend.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "채팅방 객체", description = "채팅방 생성을 위한 객체")
@Getter
@Setter
public class ChatRoomPublicRequestDto {

    @ApiModelProperty(value = "채팅방 이름", example = "오늘운동하기", required = true)
    private String name;
}
