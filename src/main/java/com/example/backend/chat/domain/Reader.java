package com.example.backend.chat.domain;

import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Reader extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean status;

    // User 로 설정 or Participant 로 설정 할지 고민...
    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    @JoinColumn
    private ChatMessage message;

    // status 는 participant 의 현재 상태에 따라 등록
    public Reader(Participant participant, ChatMessage message) {
        this.user = participant.getUser();
        this.message = message;
//        this.status = participant.isStatus();
    }

    public void read() {
        this.status = true;
    }
}
