package com.example.backend.event.domain;

import com.example.backend.common.domain.BaseTime;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Winning extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Long userId;

    @Column
    private String phone;

    @Column
    private Long productId;

    public Winning(Long userId, Long productId){
        this.userId = userId;
        this.productId = productId;
    }

    public Winning() {

    }
    public void addPhone(String phone) {
        this.phone = phone;
    }
}
