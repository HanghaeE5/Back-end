package com.example.backend.alarm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountAlarmDto {
    private Long alarmCount;

    public CountAlarmDto(Long alarmCount) {
        this.alarmCount = alarmCount;
    }
}