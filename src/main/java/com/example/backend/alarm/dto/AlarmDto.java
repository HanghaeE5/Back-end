package com.example.backend.alarm.dto;

import com.example.backend.alarm.domain.Alarm;
import com.example.backend.alarm.domain.AlarmType;
import com.example.backend.alarm.domain.ReadingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlarmDto implements Serializable {
    private Long alarmId;
    private String senderNickName;
    private AlarmType alarmType;
    private Long id;
    private String title;
    private LocalDateTime modifiedDate;
    private ReadingStatus readingStatus;

    public AlarmDto(Alarm alarm) {
        alarm.changeReadingStatus(ReadingStatus.Y);
        this.alarmId = alarm.getAlarmId();
        this.senderNickName = alarm.getSenderNickName();
        this.alarmType = alarm.getAlarmType();
        this.id = alarm.getId();
        this.title = alarm.getTitle();
        this.modifiedDate = alarm.getModifiedDate();
        this.readingStatus = alarm.getReadingStatus();
    }

    public static AlarmDto convertMessageToDto(Alarm alarm) {
        return new AlarmDto(
                alarm.getAlarmId(),
                alarm.getSenderNickName(),
                alarm.getAlarmType(),
                alarm.getId(),
                alarm.getTitle(),
                alarm.getModifiedDate(),
                alarm.getReadingStatus()
        );
    }
}