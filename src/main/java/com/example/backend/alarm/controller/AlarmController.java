package com.example.backend.alarm.controller;

import com.example.backend.alarm.dto.AlarmDto;
import com.example.backend.alarm.dto.AlarmResponseDto;
import com.example.backend.alarm.dto.CountAlarmDto;
import com.example.backend.alarm.service.AlarmService;
import com.example.backend.user.common.LoadUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AlarmController {


    private final AlarmService alarmService;


    //알람 전체조회
    @GetMapping("/api/alarm")
    public List<AlarmResponseDto> getAlarm() {
        LoadUser.loginAndNickCheck();
        return alarmService.getAlarm(LoadUser.getEmail());
    }


    //알람 리딩 안된 알람갯수 조회.
    @GetMapping("/api/alarmCount")
    public CountAlarmDto countAlarm() {
        LoadUser.loginAndNickCheck();
        return alarmService.countAlarm(LoadUser.getEmail());
    }

    //알람 리딩 전체 확인
    @PostMapping("/api/alarm")
    public List<AlarmDto> readAlarm() {
        LoadUser.loginAndNickCheck();
        return alarmService.readAlarm(LoadUser.getEmail());
    }


    //알람 삭제
    @DeleteMapping("/api/alarm/{alarmId}")
    public ResponseEntity<String> deleteAlarm(@PathVariable Long alarmId) {
        LoadUser.loginAndNickCheck();
        return ResponseEntity.ok().body(alarmService.deleteAlarm(alarmId, LoadUser.getEmail()));
    }

    //알람 전체삭제
    @DeleteMapping("/api/alarm")
    public ResponseEntity<String> deleteAllAlarm() {
        LoadUser.loginAndNickCheck();
        return ResponseEntity.ok().body(alarmService.deleteAllAlarm(LoadUser.getEmail()));
    }



}