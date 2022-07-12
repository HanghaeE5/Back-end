package com.example.backend.chat.dto.request;

import com.example.backend.chat.domain.MessageType;
import com.example.backend.common.domain.BaseTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value = "메세지 객체", description = "메세지 전송을 위한 객체")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ChatMessageRequestDto {

    @ApiModelProperty(value = "메세지 타입", example = "ENTER/TALK/QUIT", required = true)
    private MessageType type;

    @ApiModelProperty(value = "채팅방 id", required = true)
    private String roomId;

    @ApiModelProperty(value = "보내는 사람 userId")
    private String sender;

    @ApiModelProperty(value = "메세지 내용", required = true)
    private String message;

    @ApiModelProperty(value = "프로필 사진")
    private String profileImageUrl;

}
