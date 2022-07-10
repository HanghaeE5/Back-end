package com.example.backend.board.dto.request;


import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@ApiModel(value = "게시물 객체", description = "게시물 객체")
public class RequestDto {
    private BoardRequestDto board;
    private BoardTodoRequestDto todo;
}
