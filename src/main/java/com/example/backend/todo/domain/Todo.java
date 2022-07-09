package com.example.backend.todo.domain;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.BoardTodo;
import com.example.backend.board.dto.BoardTodoRequestDto;
import com.example.backend.common.domain.BaseTime;
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

    public Todo(BoardTodoRequestDto requestDto, User user, Board board, Date todoDate) {
        this.content = requestDto.getContent();
        this.todoDate = todoDate;
        this.state = false;
        this.category = requestDto.getCategory();
        this.user = user;
        this.board = board;
    }

    public Todo(BoardTodo boardTodo, User user, Board board) {
        this.content = boardTodo.getContent();
        this.todoDate = boardTodo.getTodoDate();
        this.state = false;
        this.category = boardTodo.getCategory();
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

    // CHALLENGE 게시글을 작성할 때 생겨난 boardId를 다 null 값으로 바꿔줌.
    public void changeNull() {
        this.board = null;
    }
}
