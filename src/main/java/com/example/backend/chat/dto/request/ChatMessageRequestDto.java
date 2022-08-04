package com.example.backend.chat.dto.request;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.MessageType;
import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
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

    public ChatMessageRequestDto (ChatRoom room, User user) {
        this.type = MessageType.QUIT;
        this.roomId = room.getRoomId();
        this.sender = "[알림]";
        this.message = user.getUsername() + "님이 채팅방에 입장 하셨습니다.";
    }

}
