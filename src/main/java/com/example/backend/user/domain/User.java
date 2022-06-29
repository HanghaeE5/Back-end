package com.example.backend.user.domain;

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

    @Column(nullable = false, unique = true)
    private String nick;

    @Column(nullable = false)
    private String password;

    @Column
    private String profile;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> role = new ArrayList<>();

    @Builder
    public User(String email, String nick, String password, List<String> role){
        this.email = email;
        this.nick = nick;
        this.password = password;
        this.role = role;
    }

    public User(){}
}
