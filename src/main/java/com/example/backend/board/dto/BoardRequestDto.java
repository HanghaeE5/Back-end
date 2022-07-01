package com.example.backend.board.dto;

import com.example.backend.todo.dto.TodoRequestDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class BoardRequestDto {

    private String title;
    private String content;
    private String category;
    private MultipartFile file;
    private TodoRequestDto todo;

}
