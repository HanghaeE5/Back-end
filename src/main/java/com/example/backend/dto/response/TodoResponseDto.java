package com.example.backend.dto.response;

import com.example.backend.domain.Todo;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
public class TodoResponseDto {

    private Integer todoId;
    private String todoContent;
    private Date todoDate;
    private boolean state;
    private String category;
    private LocalDateTime createdDate;

    public TodoResponseDto(Todo todo) {
        this.todoId = todo.getId();
        this.todoContent = todo.getContent();
        this.todoDate = todo.getTodoDate();
        this.state = todo.isState();
        this.category = String.valueOf(todo.getCategory());
        this.createdDate = todo.getCreatedDate();
    }
}
