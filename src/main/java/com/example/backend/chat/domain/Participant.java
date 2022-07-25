package com.example.backend.chat.domain;

import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

@Getter
@Entity
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // 가장 최근 채팅방 나간 시간
    @Column
    private LocalDateTime exitTime;

    @ManyToOne
    @JoinColumn
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn
    private User user;

    public Participant(User user, ChatRoom room) {
        this.user = user;
        this.chatRoom = room;
        this.exitTime = LocalDateTime.now(ZoneId.systemDefault());
    }

    public void exit() {
        this.exitTime = LocalDateTime.now(ZoneId.systemDefault());
    }

}
