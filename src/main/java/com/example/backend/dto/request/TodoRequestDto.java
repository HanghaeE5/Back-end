package com.example.backend.dto.request;

import com.example.backend.domain.Category;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class TodoRequestDto {

    private String content;
    private String category;
    private Integer boardId;
    private List<Date> todoDateList;
    private Date todoDate;

}
