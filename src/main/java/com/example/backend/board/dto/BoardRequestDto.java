package com.example.backend.board.dto;

import com.example.backend.board.domain.Category;
import com.example.backend.todo.dto.TodoRequestDto;
import com.example.backend.user.domain.User;
import com.example.backend.user.dto.UserResponseDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BoardRequestDto {

    private String title;
    private String content;
    private Category category;
    private MultipartFile file;
    private TodoRequestDto todo;
    private Long todoId;
    private User user;

}
