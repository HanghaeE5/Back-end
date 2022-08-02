package com.example.backend.notification.domain;

import com.example.backend.notification.dto.NotificationRequestDto;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private String message;

    @Column(nullable = false)
    private boolean readState;

    @ManyToOne
    @JoinColumn
    private User user;

    public Notification(NotificationRequestDto requestDto, User user) {
        this.type = requestDto.getType();
        this.message = requestDto.getMessage();
        this.readState = false;
        this.user = user;
    }

    public void changeState() {
        this.readState = true;
    }

}
