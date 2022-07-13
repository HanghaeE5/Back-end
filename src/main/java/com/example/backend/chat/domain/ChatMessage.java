package com.example.backend.chat.domain;

import com.example.backend.chat.dto.request.ChatMessageRequestDto;
import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column
    private String roomId;

    @Column
    private String sender;

    @Column
    private String message;

    @ManyToOne
    @JoinColumn
    private User user;

    public ChatMessage(ChatMessageRequestDto requestDto, User user) {
        this.type = requestDto.getType();
        this.roomId = requestDto.getRoomId();
        this.sender = requestDto.getSender();
        this.message = requestDto.getMessage();
        this.user = user;
    }

    public ChatMessage(ChatMessageRequestDto requestDto) {
        this.type = requestDto.getType();
        this.roomId = requestDto.getRoomId();
        this.sender = requestDto.getSender();
        this.message = requestDto.getMessage();
    }
}
