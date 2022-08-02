package com.example.backend.alarm.dto;

import com.example.backend.alarm.domain.Alarm;
import com.example.backend.alarm.domain.AlarmType;
import com.example.backend.alarm.domain.ReadingStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AlarmResponseDto {

    private Long alarmId;
    private String senderNickName;

    private Long receiverId;
    private AlarmType alarmType;
    private Long id;
    private String title;
    private String modifiedDate;
    private ReadingStatus readingStatus;
    private Integer point;

    public AlarmResponseDto(Alarm alarm) {
        this.alarmId = alarm.getAlarmId();
        this.receiverId = alarm.getReceiver().getUserSeq();
        this.senderNickName = alarm.getSenderNickName();
        this.alarmType = alarm.getAlarmType();
        this.id = alarm.getId();
        this.title = alarm.getTitle();
        this.readingStatus = alarm.getReadingStatus();
        this.point = alarm.getPoint();
    }
}