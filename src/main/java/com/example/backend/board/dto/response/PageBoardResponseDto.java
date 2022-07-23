package com.example.backend.board.dto.response;

import com.example.backend.board.domain.Board;
import lombok.Data;
import org.springframework.data.domain.Slice;

import java.util.List;

@Data
public class PageBoardResponseDto {
    private List<BoardResponseDto> content;

    private boolean last;

    public PageBoardResponseDto(List<BoardResponseDto> content, Slice<Board> boardPage){
        this.content = content;
        this.last = boardPage.isLast();
    }
}
