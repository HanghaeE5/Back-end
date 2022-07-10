package com.example.backend.character.domain;

import com.example.backend.character.dto.CharacterRequestDto;
import com.example.backend.common.domain.BaseTime;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
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
    private Integer level;

    @Column
    private Integer exp;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "USER_SEQ")
    @MapsId
    private User user;

    @OneToMany(mappedBy = "character")
    private List<Todo> todoList = new ArrayList<>();

    public Characters(CharacterRequestDto requestDto, User user) {
        this.user = user;
        this.type = requestDto.getType();
        this.step = Step.FIRST;
        this.level = 1;
        this.exp = 0;
    }

    public void addExp() {
        this.exp += 1;
    }

    public void levelUp() {
        this.exp = 0;
        this.level += 1;
    }

    public void stepUp() {
        if (this.step == Step.FIRST) {
            this.step = Step.SECOND;
        } else {
            this.step = Step.THIRD;
        }
    }

    public void addTodo(Todo todo) {
        this.todoList.add(todo);
    }
}
