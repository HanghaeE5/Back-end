package com.example.backend.character.domain;

import com.example.backend.common.domain.BaseTime;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Character extends BaseTime {

    @Id
    @OneToOne
    @JoinColumn
    private User user;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    @Enumerated(EnumType.STRING)
    private Step step;

    @Column
    private Long level;

    @Column
    private Long exp;

    @OneToMany
    private

}
