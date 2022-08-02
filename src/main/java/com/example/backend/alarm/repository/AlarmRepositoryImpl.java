package com.example.backend.alarm.repository;

import com.example.backend.alarm.domain.Alarm;
import com.example.backend.alarm.domain.ReadingStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.backend.alarm.domain.QAlarm.alarm;

public class AlarmRepositoryImpl implements AlarmRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public AlarmRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Alarm> findAlarmsByReceiver(Long userId){
        return queryFactory
                .select(alarm)
                .where(alarm.receiver.userSeq.eq(userId))
                .orderBy(alarm.modifiedDate.desc())
                .from(alarm)
                .fetch();
    }

    public Long countNotReadAlarm(Long userId){
        return queryFactory
                .select(alarm.countDistinct())
                .where(alarm.receiver.userSeq.eq(userId)
                        .and(alarm.readingStatus.eq(ReadingStatus.N)))
                .from(alarm)
                .fetchOne();
    }
}