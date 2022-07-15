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

    private String chatRoomId;

    @ApiModelProperty(value="상세 조회시, 로그인한 사용자가 참여중인지 아닌지 판별")
    private boolean participating;

    @ApiModelProperty(value="상세 조회시, 위드투두 게시물의 마감일자가 지났는지 판별 true 지남 false 않지남")
    private boolean withTodoDeadline;

    @ApiModelProperty(value="참여자 수")
    private Long ParticipatingCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime boardCreatedDate;

    private BoardTodoResponseDto todo;

    public BoardResponseDto(Board board, boolean participating, boolean withTodoDeadline) {
        this.boardId = board.getId();
        this.boardContent = board.getContent();
        this.imageUrl = board.getImageUrl();
        this.title = board.getTitle();
        this.category = board.getCategory();
        this.boardCreatedDate = board.getCreatedDate();
        this.todo = new BoardTodoResponseDto(board.getBoardTodo());
        this.authorEmail = board.getUser().getEmail();
        this.authorNick = board.getUser().getUsername();
        this.authorProfileImageUrl = board.getUser().getProfileImageUrl();
        this.participating = participating;
        this.withTodoDeadline = withTodoDeadline;
        this.ParticipatingCount = board.getParticipatingCount();
        this.chatRoomId = board.getChatRoomId();
    }

    public BoardResponseDto(Board board) {
        this.boardId = board.getId();
        this.boardContent = board.getContent();
        this.imageUrl = board.getImageUrl();
        this.title = board.getTitle();
        this.category = board.getCategory();
        this.boardCreatedDate = board.getCreatedDate();
        this.todo = new BoardTodoResponseDto(board.getBoardTodo());
        this.authorEmail = board.getUser().getEmail();
        this.authorNick = board.getUser().getUsername();
        this.authorProfileImageUrl = board.getUser().getProfileImageUrl();
        this.ParticipatingCount = board.getParticipatingCount();
        this.chatRoomId = board.getChatRoomId();
    }

    public static List<BoardResponseDto> getDtoList(List<Board> boardList){
        return boardList.stream()
                .map(board -> new BoardResponseDto(board))
                .collect(Collectors.toList());
    }
}