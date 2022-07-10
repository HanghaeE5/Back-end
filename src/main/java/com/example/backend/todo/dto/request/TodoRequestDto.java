package com.example.backend.todo.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(value = "ToDo 객체", description = "ToDo를 등록 하기 위한 객체")
public class TodoRequestDto {

    @ApiModelProperty(value = "내용", example = "1시간 운동하기", required = true)
    private String content;

    @ApiModelProperty(value = "카테고리", example = "STUDY", required = true)
    private String category;

    @ApiModelProperty(value = "게시글 id")
    private Long boardId;

    @ApiModelProperty(value = "Todo 생성 시 날짜 목록(Array)", example = "[\"2022-07-06\",\"2022-07-07\", \"2022-07-08\"]")
    private List<String> todoDateList;

    @ApiModelProperty(value = "Todo 수정 시 날짜", example = "2022-07-06")
    private String todoDate;

}
