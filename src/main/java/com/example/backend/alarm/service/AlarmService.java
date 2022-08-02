package com.example.backend.alarm.service;

import com.example.backend.alarm.domain.ReadingStatus;
import com.example.backend.alarm.dto.AlarmDto;
import com.example.backend.alarm.dto.AlarmResponseDto;
import com.example.backend.alarm.dto.CountAlarmDto;
import com.example.backend.alarm.repository.AlarmRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.msg.MsgEnum;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmService {

    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final AlarmRepository alarmRepository;
    private final UserRepository userRepository;

    public void alarmByMessage(AlarmResponseDto AlarmResponseDto) {
        redisTemplate.convertAndSend(channelTopic.getTopic(), AlarmResponseDto);
    }

    //알람 삭제
    @Transactional
    public String deleteAlarm(Long alarmId, String email) {
        User user = getUser(email);
        alarmRepository.deleteByAlarmIdAndReceiver(alarmId, user);
        return MsgEnum.DELETE_ALARM.getMsg();
    }

    //알람 전체 삭제
    @Transactional
    public String deleteAllAlarm(String email) {
        User user = getUser(email);
        alarmRepository.deleteByReceiver(user);
        return MsgEnum.DELETE_ALARM.getMsg();
    }


    //알람 전체 조회
    public List<AlarmResponseDto> getAlarm(String email) {
        User user = getUser(email);
        return alarmRepository.findAlarmsByReceiver(user.getUserSeq()).stream()
                .map(AlarmResponseDto::new)
                .collect(Collectors.toList());

    }


    //리딩 안된 알람갯수 카운트.
    public CountAlarmDto countAlarm(String email) {
        User user = getUser(email);
        Long alarmCount = alarmRepository.countByReadingStatusAndReceiver(ReadingStatus.N, user);
        return new CountAlarmDto(
                alarmCount
        );
    }

    //리딩 전체 확인하는 요청.
    @Transactional
    public List<AlarmDto> readAlarm(String email) {
        User user = getUser(email);
        return alarmRepository.findAlarmsByReceiver(user.getUserSeq())
                .stream().map(
                        AlarmDto::new)
                .collect(Collectors.toList());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }
}
