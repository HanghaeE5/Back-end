package com.example.backend.board.domain;

import com.example.backend.board.dto.request.BoardTodoRequestDto;
import com.example.backend.common.domain.BaseTime;
import com.example.backend.todo.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Entity
@Getter
public class BoardTodo extends BaseTime {

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

    public BoardTodo(BoardTodoRequestDto boardTodoRequestDto, Date todoDate, Board board){
        this.content = boardTodoRequestDto.getContent();
        this.todoDate = todoDate;
        this.category = boardTodoRequestDto.getCategory();
        this.board = board;
    }

}
