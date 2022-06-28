package com.example.backend.domain;

import com.example.backend.dto.request.TodoRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Getter
@Entity
@NoArgsConstructor
public class Todo extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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

    @OneToOne
    @JoinColumn(nullable = true)
    private Board board;

    public Todo(TodoRequestDto requestDto, UserDetailsImpl userDetails, Date tododate) {
        this.content = requestDto.getContent();
        this.todoDate = tododate;
        this.state = false;
        this.category = Category.valueOf(requestDto.getCategory());
        this.user = userDetails.getUser();
    }

    public Todo(TodoRequestDto requestDto, UserDetailsImpl userDetails, Board board, Date tododate) {
        this.content = requestDto.getContent();
        this.todoDate = tododate;
        this.state = false;
        this.category = Category.valueOf(requestDto.getCategory());
        this.user = userDetails.getUser();
        this.board = board;
    }

    public void update(TodoRequestDto requestDto) {

        this.content = requestDto.getContent();
        this.todoDate = requestDto.getTodoDate();
        this.category = Category.valueOf(requestDto.getCategory());

    }

    public void done() {
        state = !state;
    }
}
