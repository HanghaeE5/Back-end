package com.example.backend.chat.domain;

import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
        this.exitTime = LocalDateTime.now();
    }

    public void exit() {
        this.exitTime = LocalDateTime.now();
    }

}
