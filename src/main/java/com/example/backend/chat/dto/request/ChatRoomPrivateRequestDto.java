package com.example.backend.chat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@ApiModel(value = "일대일 채팅방 객체", description = "채팅방 생성을 위한 객체")
@Getter
@Setter
public class ChatRoomPrivateRequestDto {

    @ApiModelProperty(value = "채팅방 이름(상대방 닉네임)", example = "hesu", required = true)
    private String name;

    @ApiModelProperty(value = "채팅 상대", example = "hesu", required = true)
    private String nick;

}
