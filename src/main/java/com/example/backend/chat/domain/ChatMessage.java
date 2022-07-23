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

    // 처음 생성 시 participantCount 를 전체 인원에서 빼서 넣음
    // 새로운 사람이 연결되면 그 사람 exitTime 을 고려해서 변경
    @Column
    private long notRead;

    @ManyToOne
    @JoinColumn
    private User user;

    public ChatMessage(ChatMessageRequestDto requestDto, User user, long notRead) {
        this.type = requestDto.getType();
        this.roomId = requestDto.getRoomId();
        this.sender = user.getUsername();
        this.message = requestDto.getMessage();
        this.user = user;
        this.notRead = notRead;
    }

    public ChatMessage(ChatMessageRequestDto requestDto) {
        this.type = requestDto.getType();
        this.roomId = requestDto.getRoomId();
        this.sender = requestDto.getSender();
        this.message = requestDto.getMessage();
    }
}
