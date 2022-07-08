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

    // 전체 게시글 목록 조회
    @ApiOperation(value = "전체 게시글 목록 조회")
    @ApiImplicitParams({
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
    // 게시글 상세 조회
    @ApiOperation(value = "게시글 상세 조회")
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponseDto> getDetailBoard(
            @PathVariable Long id
    ) {
        LoadUser.loginAndNickCheck();
        BoardResponseDto responseDto = boardService.getDetailBoard(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    // 게시글 작성
    @ApiOperation(value = "게시글 작성")
    @PostMapping("/board")
    public ResponseEntity<String> postBoard(
            @Valid @RequestPart(value = "board") BoardRequestDto board,
            @RequestPart(value = "todo", required = false) TodoRequestDto todo,
            @RequestPart(value = "file", required = false) MultipartFile file
            ) throws Exception {
        LoadUser.loginAndNickCheck();
        boardService.save(board, todo, file, LoadUser.getEmail());
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.BOARD_SAVE_SUCCESS.getMsg());
    }
    // 게시글 삭제
    @ApiOperation(value = "게시글 삭제") // swagger
    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long id
    ) {
        LoadUser.loginAndNickCheck();
        boardService.deleteBoard(id,LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.BOARD_DELETE_SUCCESS.getMsg());
    }
    // 게시글 수정
//    @ApiOperation(value = "게시글 수정")
//    @PutMapping("/board/{id}")
//    public ResponseEntity<String> updateBoard(
//            @PathVariable Long id,
//            @RequestBody BoardRequestDto requestDto,
//            @RequestPart(value = "file", required = false) MultipartFile file
//    ) throws Exception {
//        LoadUser.loginAndNickCheck();
//        boardService.updateBoard(id, requestDto, LoadUser.getEmail(), file);
//        return ResponseEntity.status(HttpStatus.OK).body("게시글이 수정되었습니다.");
//    }
    // 게시물 검색
//    @GetMapping("/board/search")
//    public ResponseEntity<Page<BoardResponseDto>> searchBoard(
//            @RequestParam String classify,
//            @RequestParam String keyword,
//            @RequestParam String filter,
//            @RequestParam Integer page,
//            @RequestParam Integer size,
//            @RequestParam String sort
//    ) {
//        Page<BoardResponseDto> responseDto =  boardService.searchBoard(
//                classify, keyword, filter,
//                page, size, sort);
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
}
