package com.example.backend.chat.domain;

import com.example.backend.chat.dto.request.ChatRoomPublicRequestDto;
import com.example.backend.common.domain.BaseTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

    // 일대일 채팅방 보류
//    public ChatRoom(ChatRoomPrivateRequestDto requestDto) {
//        this.roomId = UUID.randomUUID().toString();
//        this.name = requestDto.getName();
//    }

    public ChatRoom(ChatRoomPublicRequestDto requestDto) {
        this.roomId = UUID.randomUUID().toString();
        this.name = requestDto.getName();
    }

    public void addParticipant(Participant participant) {
        this.participantList.add(participant);
    }
}
