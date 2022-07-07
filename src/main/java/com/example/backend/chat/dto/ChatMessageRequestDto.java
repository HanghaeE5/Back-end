package com.example.backend.chat.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@ApiModel(value = "메세지 객체", description = "메세지 전송을 위한 객체")
@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDto {

    @ApiModelProperty(value = "메세지 타입", example = "ENTER/TALK/QUIT", required = true)
    private MessageType type;

    @ApiModelProperty(value = "채팅방 id", required = true)
    private String roomId;

    @ApiModelProperty(value = "보내는 사람 userId")
    private String sender;

    @ApiModelProperty(value = "메세지 내용", required = true)
    private String message;

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @Builder
    public ChatMessageRequestDto(MessageType type, String roomId, String sender, String message) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}
