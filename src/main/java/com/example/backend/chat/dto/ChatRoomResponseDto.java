package com.example.backend.chat.dto;

import com.example.backend.chat.domain.ChatRoom;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoomResponseDto {
    private String roomId;
    private String name;

    public ChatRoomResponseDto(ChatRoom room) {
        this.roomId = room.getRoomId();
        this.name = room.getName();
    }
}
