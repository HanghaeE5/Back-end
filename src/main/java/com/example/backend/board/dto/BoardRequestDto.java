package com.example.backend.board.dto;

import com.example.backend.board.domain.Category;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "게시물 객체", description = "게시물을 등록 하기 위한 객체")
@Getter
public class BoardRequestDto {

    @ApiModelProperty(value="제목", example = "하루 3시간씩 운동하기", required = true)
    @NotBlank(message = "제목은 필수 입력값 입니다.")
    private String title;

    @ApiModelProperty(value="내용", example = "하루 3시간씩 운동하기 관련 내용", required = true)
    @NotBlank(message = "내용은 필수 입력값 입니다.")
    private String content;

    @ApiModelProperty(value="카테고리", example = "DAILY/CHALLENGE", required = true)
    @NotBlank(message = "카테고리는 필수 입력값 입니다.")
    private String category;

}
