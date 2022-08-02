package com.example.backend.alarm.repository;

import com.example.backend.alarm.domain.Alarm;
import com.example.backend.alarm.domain.ReadingStatus;
import com.example.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface AlarmRepository extends JpaRepository<Alarm, Long>, AlarmRepositoryCustom {
    //알람 전체 삭제
    void deleteByReceiver(User user);
    //알람 삭제
    void deleteByAlarmIdAndReceiver(Long alarmId, User user);

    //알람 갯수 파악
    Long countByReadingStatusAndReceiver(ReadingStatus N, User user);
}