package com.example.backend.todo.domain;

import com.example.backend.board.domain.Board;
import com.example.backend.common.BaseTime;
import com.example.backend.user.domain.User;
import com.example.backend.todo.dto.TodoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
public class Todo extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Date todoDate;

    @Column(nullable = false)
    private boolean state;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = true)
    private Board board;

    public Todo(TodoRequestDto requestDto, User user, Date tododate) {
        this.content = requestDto.getContent();
        this.todoDate = tododate;
        this.state = false;
        this.category = Category.valueOf(requestDto.getCategory());
        this.user = user;
    }

    public Todo(TodoRequestDto requestDto, User user, Board board, Date tododate) {
        this.content = requestDto.getContent();
        this.todoDate = tododate;
        this.state = false;
        this.category = Category.valueOf(requestDto.getCategory());
        this.user = user;
        this.board = board;
    }

    public void update(TodoRequestDto requestDto, Date date) {

        this.content = requestDto.getContent();
        this.todoDate = date;
        this.category = Category.valueOf(requestDto.getCategory());

    }

    public void done() {
        state = !state;
    }
}
