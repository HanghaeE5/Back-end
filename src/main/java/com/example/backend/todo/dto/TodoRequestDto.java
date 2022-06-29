package com.example.backend.todo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class TodoRequestDto {

    private String content;
    private String category;
    private Integer boardId;
    private List<String> todoDateList;
    private String todoDate;

}
