package com.example.backend.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User extends BaseTime{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String nick;

    @Column(nullable = false)
    private String password;

    private String profile;

    @Column(unique = true)
    private Long kakaoId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role = new ArrayList<>();

    @Builder
    public User(String email, String nick, String password, List<String> role){
        this.email = email;
        this.nick = nick;
        this.password = password;
        this.role = role;
        this.kakaoId = null;
    }

    @Builder
    public User(String password, String email, List<String> role, Long kakaoId){
        this.email = email;
        this.nick = null;
        this.password = password;
        this.role = role;
        this.kakaoId = kakaoId;
    }

    public void userToKakaoUser(Long kakaoId){
        this.kakaoId = kakaoId;
    }

    public void addNick(String nick){
        this.nick = nick;
    }

    public User(){}
}
