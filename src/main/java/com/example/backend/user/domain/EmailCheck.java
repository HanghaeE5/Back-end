package com.example.backend.user.domain;

import com.example.backend.common.domain.BaseTime;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
public class EmailCheck extends BaseTime {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String email;

    private String code;

    @Column(columnDefinition = "varchar(2) default 'N'")
    private String confirmYn;

    public EmailCheck(String email, String code){
        this.email = email;
        this.code = code;
    }

    public EmailCheck() {

    }

    public void verificationCompleted(String confirmYn){
        this.confirmYn = confirmYn;
    }
}
