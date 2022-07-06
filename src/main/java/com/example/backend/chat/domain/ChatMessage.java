package com.example.backend.chat.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    private MessageType type;
    private String roomId;
    private String sender;
    private String message;

    public enum MessageType {
        ENTER, TALK, QUIT
    }

    @Builder
    public ChatMessage(MessageType type, String roomId, String sender, String message) {
        this.type = type;
        this.roomId = roomId;
        this.sender = sender;
        this.message = message;
    }
}
