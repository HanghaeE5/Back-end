package com.example.backend.chat.domain;

import com.example.backend.user.domain.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String Long;

    @ManyToOne
    @JoinColumn
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn
    private User user;

    public Participant(User user, ChatRoom room) {
        this.user = user;
        this.chatRoom = room;
    }
}
