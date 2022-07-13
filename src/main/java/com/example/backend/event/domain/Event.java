package com.example.backend.event.domain;

import com.example.backend.common.domain.BaseTime;
import lombok.Getter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
public class Event extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private boolean isSchedule;

    @Column
    private Date runTime;

    public Event(Date runTime){
        this.isSchedule = true;
        this.runTime = runTime;
    }

    public Event() {

    }
}
