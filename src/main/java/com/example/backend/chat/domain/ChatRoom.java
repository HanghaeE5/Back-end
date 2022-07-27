package com.example.backend.chat.domain;

import com.example.backend.chat.dto.request.ChatRoomPrivateRequestDto;
import com.example.backend.common.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime lastMessage;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "chatRoom")
    private List<Participant> participantList = new ArrayList<>();

    public ChatRoom(ChatRoomPrivateRequestDto requestDto) {
        this.roomId = UUID.randomUUID().toString();
        this.name = requestDto.getName();
        this.type = Type.PRIVATE;
        this.lastMessage = LocalDateTime.now();
    }

    public ChatRoom(String name) {
        this.roomId = UUID.randomUUID().toString();
        this.name = name;
        this.type = Type.PUBLIC;
        this.lastMessage = LocalDateTime.now();
    }

    public void addParticipant(Participant participant) {
        this.participantList.add(participant);
    }

    public void newMessage() {
        this.lastMessage = LocalDateTime.now();
    }
}
