package com.example.backend.board.domain;

import com.example.backend.todo.domain.Category;
import com.example.backend.todo.dto.request.TodoRequestDto;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Entity
public class BoardTodo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Date todoDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @ManyToOne
    @JoinColumn
    private Board board;

    public BoardTodo(TodoRequestDto todoRequestDto, Date todoDate, Board board){
        this.content = todoRequestDto.getContent();
        this.todoDate = todoDate;
        this.category = Category.valueOf(todoRequestDto.getCategory());
        this.board = board;
    }
}
