package com.example.backend.chat.dto.response;

import com.example.backend.chat.domain.ChatMessage;
import com.example.backend.chat.domain.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageResponseDto {

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    public ChatMessageResponseDto(ChatMessage message) {
        this.type = message.getType();
        this.roomId = message.getRoomId();
        this.sender = message.getSender();
        this.message = message.getMessage();
        this.createdDate = message.getCreatedDate();
    }

}
