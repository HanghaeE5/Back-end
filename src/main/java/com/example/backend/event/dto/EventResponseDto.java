package com.example.backend.event.dto;

import com.example.backend.event.domain.Stamp;
import com.example.backend.event.domain.StampDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class EventResponseDto {
    private int stampCnt;
    private int couponCnt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private List<Date> stampDates = new ArrayList<>();

    public EventResponseDto(Stamp stamp){
        this.stampCnt = stamp.getStamp();
        this.couponCnt = stamp.getCoupon();
        for (StampDate stampDate : stamp.getStampDates()){
            this.stampDates.add(stampDate.getStampDate());
        }
    }
}
