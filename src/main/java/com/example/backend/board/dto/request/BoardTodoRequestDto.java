package com.example.backend.board.dto.request;

import com.example.backend.todo.domain.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import java.util.List;

@Getter
@ApiModel(value = "Board ToDo 객체", description = "Board ToDo를 등록 하기 위한 객체")
public class BoardTodoRequestDto {

    @ApiModelProperty(value = "내용", example = "2시간 운동하기", required = true)
    private String content;

    @ApiModelProperty(value = "카테고리", example = "STUDY/EXERCISE", required = true)
    private Category category;

    @ApiModelProperty(value = "Todo 생성 시 날짜 목록(Array)", example = "[\"2022-07-06\",\"2022-07-07\", \"2022-07-08\"]")
    private List<String> todoDateList;
}