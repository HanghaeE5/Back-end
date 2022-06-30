package com.example.backend.todo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TodoRequestDto {

    private String content;
    private String category;
    private Integer boardId;
    private List<String> todoDateList;
    private String todoDate;

}
