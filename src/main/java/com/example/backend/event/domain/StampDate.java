package com.example.backend.event.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class StampDate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Date stampDate;

    @ManyToOne
    @JoinColumn
    private Stamp stamp;


    public StampDate(Date stampDate, Stamp stamp){
        this.stampDate = stampDate;
        this.stamp = stamp;
    }

}
