package com.example.backend.todo.dto;

import com.example.backend.todo.domain.Todo;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Data
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
