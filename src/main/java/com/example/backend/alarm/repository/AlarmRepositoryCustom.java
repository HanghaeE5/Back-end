package com.example.backend.alarm.repository;

import com.example.backend.alarm.domain.Alarm;

import java.util.List;

public interface AlarmRepositoryCustom {

    List<Alarm> findAlarmsByReceiver(Long userId);

    Long countNotReadAlarm(Long userId);
}
