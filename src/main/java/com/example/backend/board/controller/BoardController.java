package com.example.backend.board.controller;

import com.example.backend.board.dto.BoardRequestDto;
import com.example.backend.board.dto.BoardResponseDto;
import com.example.backend.board.service.BoardService;
import com.example.backend.user.common.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 전체 게시글 목록 조회
    @GetMapping("/board")
    public ResponseEntity<Page<BoardResponseDto>> getBoardList(
            @RequestParam String filter,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<BoardResponseDto> responseDtoList = boardService.getBoardList(
                filter, page, size, sort, userDetails
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }
    // 게시글 상세 조회
    @GetMapping("/board/{id}")
    public ResponseEntity<BoardResponseDto> getDetailBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        BoardResponseDto responseDto = boardService.getDetailBoard(id, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
    // 게시글 작성
    @PostMapping("/board")
    public ResponseEntity<String> postBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(value = "board") String board,
            @RequestParam(value = "todo", required = false) String todo,
            @RequestPart(value = "file", required = false) MultipartFile file

            ) throws IOException, ParseException {

        boardService.save(board, todo, file, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("파일 업로드 및 게시글 작성 완료");
    }
    // 게시글 삭제
    @DeleteMapping("/board/{id}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.deleteBoard(id, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 삭제되었습니다.");

    }
    // 게시글 수정
    @PutMapping("/board/{id}")
    public ResponseEntity<String> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        boardService.updateBoard(id, requestDto, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("게시글이 수정되었습니다.");
    }
    // 게시물 검색
//    @GetMapping("/board/search")
//    public ResponseEntity<Page<BoardResponseDto>> searchBoard(
//            @RequestParam String classify,
//            @RequestParam String keyword,
//            @RequestParam String filter,
//            @RequestParam Integer page,
//            @RequestParam Integer size,
//            @RequestParam String sort,
//            @AuthenticationPrincipal UserDetailsImpl userDetails
//    ) {
//        Page<BoardResponseDto> responseDto =  boardService.searchBoard(
//                classify, keyword, filter,
//                page, size, sort, userDetails);
//        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//    }
}
