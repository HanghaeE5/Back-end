package com.example.backend.board.dto.response;

import com.example.backend.board.domain.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardResponseDtoV2 {
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
}
