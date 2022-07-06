package com.example.backend.todo.dto;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "ToDo 객체", description = "ToDo를 등록 하기 위한 객체")
public class TodoRequestDto {

    private String content;
    private String category;
    private Long boardId;
    private List<String> todoDateList;
    private String todoDate;

}
