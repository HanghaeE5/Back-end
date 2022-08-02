package com.example.backend.alarm.domain;

import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Alarm extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long alarmId;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "sender_nickName")
    private String senderNickName;

    @Column
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;

    @Column
    private Long id;

    @Column
    private Integer point = null;

    @Column
    private String title;

    @Enumerated(EnumType.STRING)
    private ReadingStatus readingStatus;


    public Alarm(User receiver, String senderNickName, AlarmType alarmType, Long id, String title) {
        this.receiver = receiver;
        this.senderNickName = senderNickName;
        this.alarmType = alarmType;
        this.id = id;
        this.title = title;
        this.readingStatus = ReadingStatus.N;
    }

    public Alarm(User receiver, String senderNickName, AlarmType alarmType, Long id, String title, int point) {
        this.receiver = receiver;
        this.senderNickName = senderNickName;
        this.alarmType = alarmType;
        this.id = id;
        this.title = title;
        this.readingStatus = ReadingStatus.N;
        this.point = point;
    }

    public void changeReadingStatus(ReadingStatus readingStatus) {
        this.readingStatus = readingStatus;
    }

}