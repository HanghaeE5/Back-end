package com.example.backend.event.domain;

import com.example.backend.chat.domain.Participant;
import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Stamp extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private int stamp;

    @Column
    private int coupon;

    @OneToMany(mappedBy = "stamp", fetch = FetchType.LAZY)
    private List<StampDate> stampDates = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_SEQ")
    private User user;

    public Stamp(User user){
        this.user = user;
        this.stamp = 0;
        this.coupon = 0;
    }

    public void addStamp(StampDate stampDate){
        this.stamp += 1;
        this.stampDates.add(stampDate);
    }

    public void stampToCoupon(){
        this.stamp -= 3;
        this.coupon += 1;
    }

    public void openBox(){
        this.coupon -= 1;
    }
}
