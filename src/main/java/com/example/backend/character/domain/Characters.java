package com.example.backend.character.domain;

import com.example.backend.common.domain.BaseTime;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Characters extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    @Enumerated(EnumType.STRING)
    private Step step;

    @Column
    private Long level;

    @Column
    private int exp;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "USER_SEQ")
    @MapsId
    private User user;

    @OneToMany(mappedBy = "character")
    private List<Todo> todoList;

    public Characters(User user, Type type) {
        this.user = user;
        this.type = type;
        this.step = Step.FIRST;
        this.level = 1L;
        this.exp = 0;
    }
}
