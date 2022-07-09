package com.example.backend.board.controller;

import com.example.backend.board.dto.*;
import com.example.backend.board.service.BoardService;
import com.example.backend.msg.MsgEnum;
import com.example.backend.todo.dto.TodoRequestDto;
import com.example.backend.user.common.LoadUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @ApiOperation(value = "게시글 이미지 저장")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @PostMapping(value = "/board/image")
    public ResponseEntity<String> saveImage(@RequestPart(value = "file") MultipartFile file){
        LoadUser.loginAndNickCheck();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(boardService.saveImage(file));
    }

    @ApiOperation(value = "게시글 작성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @PostMapping("/board")
    public ResponseEntity<String> postBoard(
            @Valid @RequestBody RequestDto requestDto
    ) throws Exception {
        LoadUser.loginAndNickCheck();

        boardService.save(requestDto, LoadUser.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.BOARD_SAVE_SUCCESS.getMsg());
    }

    @ApiOperation(value = "전체 게시글 목록 조회")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
        @ApiImplicitParam(name = "filter", value = "카테고리(daily/challenge/my)", dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "keyword", value = "검색 키워드", dataType = "string", paramType = "query"),
        @ApiImplicitParam(name = "pageable", value = "페이징 값(size, page, sort)", dataType = "Pageable", paramType = "query"),
        @ApiImplicitParam(name = "sub", value = "(제목/내용으로 검색)title/content", dataType = "string", paramType = "query")
    })
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @GetMapping("/board")
    public ResponseEntity<PageBoardResponseDto> getBoardList(
            @RequestParam(defaultValue = "all", required = false) FilterEnum filter,
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "noSearch", required = false) SubEnum sub,
            @PageableDefault(sort="createdDate", direction= Sort.Direction.DESC) Pageable pageable
    ) {
        LoadUser.loginAndNickCheck();
        PageBoardResponseDto responseDto = boardService.getBoardList(
                filter, keyword, pageable, LoadUser.getEmail(), sub
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @ApiOperation(value = "게시글 상세 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @GetMapping("/board/{boardId}")
    public ResponseEntity<BoardResponseDto> getDetailBoard(
            @PathVariable Long boardId
    ) {
        LoadUser.loginAndNickCheck();
        return ResponseEntity.status(HttpStatus.OK).body(boardService.getDetailBoard(boardId));
    }

    @ApiOperation(value = "게시글 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @DeleteMapping("/board/{boardId}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long boardId
    ) {
        LoadUser.loginAndNickCheck();
        boardService.deleteBoard(boardId,LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.BOARD_DELETE_SUCCESS.getMsg());
    }

    @ApiOperation(value = "게시글 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token"),
    })
    @PutMapping("/board/{boardId}")
    public ResponseEntity<String> updateBoard(
            @PathVariable Long boardId,
            @Valid @RequestBody RequestDto requestDto
    ) throws Exception {
        LoadUser.loginAndNickCheck();
        boardService.updateBoard(boardId, LoadUser.getEmail(), requestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.BOARD_UPDATE_SUCCESS.getMsg());
    }

    //챌린저스 신청 api
    @ApiOperation(value = "첼린지 신청")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @PostMapping("/board/{boardId}/challenge")
    public ResponseEntity<String> applyChallenge(
        @PathVariable Long boardId
    ) throws Exception{
        LoadUser.loginAndNickCheck();
        boardService.applyChallenge(boardId, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.CHALLENGE_APPLY_SUCCESS.getMsg());
    }

    //챌린저스 취소 api
    @ApiOperation(value = "첼린지 취소")
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @PutMapping("/board/{boardId}/challenge")
    public ResponseEntity<String> cancelChallenge(
            @PathVariable Long boardId
    ) throws Exception{
        LoadUser.loginAndNickCheck();
        boardService.cancelChallenge(boardId, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.CHALLENGE_CANCEL_SUCCESS.getMsg());
    }
}
