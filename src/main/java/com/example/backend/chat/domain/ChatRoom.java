package com.example.backend.chat.domain;

import com.example.backend.chat.dto.ChatRoomRequestDto;
import com.example.backend.common.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatRoom extends BaseTime {

    @Id
    private String roomId;

    @Column
    private String name;

    @OneToMany(mappedBy = "chatRoom")
    private List<Participant> participantList;

    public ChatRoom(ChatRoomRequestDto requestDto) {
        this.roomId = UUID.randomUUID().toString();
        this.name = requestDto.getName();
    }
}
