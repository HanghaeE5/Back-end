package com.example.backend.board.dto.response;

import com.example.backend.board.domain.Board;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Getter
public class PageBoardResponseDto {
    private List<BoardResponseDto> content;

    private boolean last;

    private Long totalElements;

    public PageBoardResponseDto(List<BoardResponseDto> content, Page<Board> boardPage){
        this.content = content;
        this.last = boardPage.isLast();
        this.totalElements = boardPage.getTotalElements();
    }
}
