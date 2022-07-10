package com.example.backend.todo.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(value = "Todo 공개범위 설정 객체", description = "Todo 의 공개범위 설정을 위한 객체")
public class TodoScopeRequestDto {

    @ApiModelProperty(value = "공개범위", example = "ALL", required = true)
    private String publicScope;

}
