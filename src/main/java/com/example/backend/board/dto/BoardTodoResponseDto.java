package com.example.backend.board.dto;

import com.example.backend.board.domain.BoardTodo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Data
public class BoardTodoResponseDto {
    private Long id;
    private String todoContent;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date todoDate;

    private String category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    public BoardTodoResponseDto(BoardTodo boardTodo){
        this.id = boardTodo.getId();
        this.todoContent = boardTodo.getContent();
        this.category = String.valueOf(boardTodo.getCategory());
        this.todoDate = boardTodo.getTodoDate();
        this.createdDate = boardTodo.getCreatedDate();
    }

    public static List<BoardTodoResponseDto> getBoardTodoList(Set<BoardTodo> boardTodoList){
        return boardTodoList.stream()
                .map(BoardTodoResponseDto::new)
                .collect(Collectors.toList());
    }
}
