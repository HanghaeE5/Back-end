package com.example.backend.chat.domain;

import com.example.backend.chat.dto.ChatRoomPrivateRequestDto;
import com.example.backend.chat.dto.ChatRoomPublicRequestDto;
import com.example.backend.common.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @OneToMany(mappedBy = "chatRoom")
    private List<Participant> participantList = new ArrayList<>();

    public ChatRoom(ChatRoomPrivateRequestDto requestDto) {
        this.roomId = UUID.randomUUID().toString();
        this.name = requestDto.getName();
    }

    public ChatRoom(ChatRoomPublicRequestDto requestDto) {
        this.roomId = UUID.randomUUID().toString();
        this.name = requestDto.getName();
    }

    public void addParticipant(Participant participant) {
        this.participantList.add(participant);
    }
}
