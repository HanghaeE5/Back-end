package com.example.backend.chat.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "채팅방 나가기 객체", description = "채팅방 나가기 위한 객체")
public class ChatRoomExitRequestDto {

    @ApiModelProperty(value = "채팅방 id", required = true)
    private String roomId;

}
