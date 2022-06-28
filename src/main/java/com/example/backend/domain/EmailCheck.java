package com.example.backend.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
public class EmailCheck extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String code;

    @Builder
    public EmailCheck(String email, String code){
        this.email = email;
        this.code = code;
    }

    public EmailCheck() {

    }
}
