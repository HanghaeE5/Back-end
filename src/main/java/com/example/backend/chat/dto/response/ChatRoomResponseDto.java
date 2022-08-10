package com.example.backend.chat.dto.response;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.Participant;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatRoomResponseDto implements Comparable<ChatRoomResponseDto> {
    private String roomId;
    private String name;
    private List<ParticipantResponseDto> participantList = new ArrayList<>();
    private LocalDateTime lastMessage;
    private boolean newMessage;

    public ChatRoomResponseDto(ChatRoom room, User user) {
        this.roomId = room.getRoomId();
        this.name = room.getName();
        for (Participant p : room.getParticipantList()) {
            // Front 에서 채팅방 사진 설정을 위해 자기 자신의 정보는 Response 에서 제외 요청
            if (p.getUser() == user) {
                continue;
            }
            this.participantList.add(new ParticipantResponseDto(p));
        }
        this.lastMessage = room.getLastMessage();
    }

    public ChatRoomResponseDto(ChatRoom room, User user, boolean newMessage) {
        this.roomId = room.getRoomId();
        this.name = room.getName();
        for (Participant p : room.getParticipantList()) {
            // Front 에서 채팅방 사진 설정을 위해 자기 자신의 정보는 Response 에서 제외 요청
            if (p.getUser() == user) {
                continue;
            }
            this.participantList.add(new ParticipantResponseDto(p));
        }
        this.lastMessage = room.getLastMessage();
        this.newMessage = newMessage;
    }

    @Override
    public int compareTo(ChatRoomResponseDto dto) {
        if (lastMessage == null || dto.getLastMessage() == null) {
            return 0;
        }
        return dto.getLastMessage().compareTo(lastMessage);
    }

}
