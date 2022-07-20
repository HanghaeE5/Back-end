package com.example.backend.chat.domain;

import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    // true : 채팅방 보는중 / false : 채팅방 안보는 중
//    @Column
//    private boolean status;
//
//    // 가장 최근 채팅방 나간 시간
//    @Column
//    private Date exitTime;

    @ManyToOne
    @JoinColumn
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn
    private User user;

    public Participant(User user, ChatRoom room) {
        this.user = user;
        this.chatRoom = room;
//        this.status = false;
    }

//    public void connect() {
//        this.status = true;
//    }
//
//    public void disConnect() {
//        this.status = false;
//    }
}
