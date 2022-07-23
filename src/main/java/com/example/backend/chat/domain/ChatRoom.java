package com.example.backend.chat.domain;

import com.example.backend.chat.dto.request.ChatRoomPrivateRequestDto;
import com.example.backend.chat.dto.request.ChatRoomPublicRequestDto;
import com.example.backend.common.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class ChatRoom extends BaseTime {

    @Id
    private String roomId;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chatRoom")
    private List<Participant> participantList = new ArrayList<>();

    public ChatRoom(ChatRoomPrivateRequestDto requestDto) {
        this.roomId = UUID.randomUUID().toString();
        this.name = requestDto.getName();
        this.type = Type.PRIVATE;
    }

    public ChatRoom(ChatRoomPublicRequestDto requestDto) {
        this.roomId = UUID.randomUUID().toString();
        this.name = requestDto.getName();
        this.type = Type.PUBLIC;
    }

    public ChatRoom(String name) {
        this.roomId = UUID.randomUUID().toString();
        this.name = name;
        this.type = Type.PUBLIC;
    }

    public void addParticipant(Participant participant) {
        this.participantList.add(participant);
    }
}
