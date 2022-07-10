package com.example.backend.chat.dto.response;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.Participant;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatRoomResponseDto {
    private String roomId;
    private String name;
    private List<ParticipantResponseDto> participantList = new ArrayList<>();

    public ChatRoomResponseDto(ChatRoom room) {
        this.roomId = room.getRoomId();
        this.name = room.getName();
        for (Participant p : room.getParticipantList()) {
            this.participantList.add(new ParticipantResponseDto(p));
        }
    }
}
