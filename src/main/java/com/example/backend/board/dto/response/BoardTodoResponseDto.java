package com.example.backend.board.dto.response;

import com.example.backend.board.domain.BoardTodo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Data
public class BoardTodoResponseDto {
    private String todoContent;

    private String category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private List<Date> todoDateList = new ArrayList<>();

    public BoardTodoResponseDto(Set<BoardTodo> boardTodo){
        for(BoardTodo bt : boardTodo){
            this.todoContent = bt.getContent();
            this.category = bt.getCategory().toString();
            this.todoDateList.add(bt.getTodoDate());
        }
        Collections.sort(this.todoDateList);
    }

    public BoardTodoResponseDto(BoardTodo boardTodo){
        this.todoContent = boardTodo.getContent();
        this.category = boardTodo.getCategory().toString();
        this.todoDateList.add(boardTodo.getTodoDate());
    }

//    public static List<BoardTodoResponseDto> getBoardTodoList(Set<BoardTodo> boardTodoList){
//        return boardTodoList.stream()
//                .map(BoardTodoResponseDto::new)
//                .collect(Collectors.toList());
//    }
}
