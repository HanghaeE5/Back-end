package com.example.backend.board.dto.response;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
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

    private String authorEmail;

    private String authorNick;

    private String authorProfileImageUrl;

    @ApiModelProperty(value="단일 건 조회시, 참여중인지 아닌")
    private boolean isParticipating;

    @ApiModelProperty(value="참여자 수")
    private Long ParticipatingCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime boardCreatedDate;

    private List<BoardTodoResponseDto> todos = new ArrayList<>();

    public BoardResponseDto(Board board, boolean isParticipating) {
        this.boardId = board.getId();
        this.boardContent = board.getContent();
        this.imageUrl = board.getImageUrl();
        this.title = board.getTitle();
        this.category = board.getCategory();
        this.boardCreatedDate = board.getCreatedDate();
        this.todos = BoardTodoResponseDto.getBoardTodoList(board.getBoardTodo());
        this.authorEmail = board.getUser().getEmail();
        this.authorNick = board.getUser().getUsername();
        this.authorProfileImageUrl = board.getUser().getProfileImageUrl();
        this.isParticipating = isParticipating;
        this.ParticipatingCount = board.getParticipatingCount();

    }

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.boardContent = board.getContent();
        this.imageUrl = board.getImageUrl();
        this.title = board.getTitle();
        this.category = board.getCategory();
        this.boardCreatedDate = board.getCreatedDate();
        this.todos = BoardTodoResponseDto.getBoardTodoList(board.getBoardTodo());
        this.authorEmail = board.getUser().getEmail();
        this.authorNick = board.getUser().getUsername();
        this.authorProfileImageUrl = board.getUser().getProfileImageUrl();
        this.ParticipatingCount = board.getParticipatingCount();
    }

    public static List<BoardResponseDto> getDtoList(List<Board> boardList){
        return boardList.stream()
                .map(board -> new BoardResponseDto(board))
                .collect(Collectors.toList());
    }
}