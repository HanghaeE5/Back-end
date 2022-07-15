package com.example.backend.character.domain;

import com.example.backend.character.dto.CharacterRequestDto;
import com.example.backend.common.domain.BaseTime;
import com.example.backend.todo.domain.Category;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.domain.PublicScope;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    // 현재 레벨에서 완료한 개수
    @Column
    private Integer exp;

    @ColumnDefault(value = "0")
    @Column
    private Integer study;

    @ColumnDefault(value = "0")
    @Column
    private Integer exercise;

    @ColumnDefault(value = "0")
    @Column
    private Integer promise;

    @ColumnDefault(value = "0")
    @Column
    private Integer shopping;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
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
        this.study = 0;
        this.exercise = 0;
        this.promise = 0;
        this.shopping = 0;
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

    public void upgradeMedal(Category category) {
        if (category == Category.STUDY) {
            this.study++;
        } else if (category == Category.EXERCISE) {
            this.exercise++;
        } else if (category == Category.PROMISE) {
            this.promise++;
        } else {
            this.shopping++;
        }
    }

}
