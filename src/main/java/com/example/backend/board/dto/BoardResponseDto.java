package com.example.backend.board.dto;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Data
@NoArgsConstructor
public class BoardResponseDto {
    private Long boardId;
    private String boardContent;
    private String imageUrl;
    private String title;
    private Category category;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime boardCreatedDate;

    private List<BoardTodoResponseDto> todos = new ArrayList<>();

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.boardContent = board.getContent();
        this.imageUrl = board.getImageUrl();
        this.title = board.getTitle();
        this.category = board.getCategory();
        this.boardCreatedDate = board.getCreatedDate();
        this.todos = BoardTodoResponseDto.getBoardTodoList(board.getBoardTodo());
    }
    public static List<BoardResponseDto> getDtoList(List<Board> boardList){
        return boardList.stream()
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }
}