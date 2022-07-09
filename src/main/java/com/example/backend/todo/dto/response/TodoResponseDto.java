package com.example.backend.todo.dto.response;

import com.example.backend.todo.domain.Todo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TodoResponseDto {

    private Long todoId;
    private String todoContent;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date todoDate;
    private boolean state;
    private String category;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
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
