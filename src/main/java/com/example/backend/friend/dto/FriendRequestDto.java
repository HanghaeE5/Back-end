package com.example.backend.friend.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "친구 요청 객체", description = "친구 요청을 보내기 위한 객체")
public class FriendRequestDto {
    @ApiModelProperty(value = "닉네임", example = "hesu", required = true)
    private String nick;
}
