package com.example.backend.board.controller;

import com.example.backend.board.service.BoardService;
import com.example.backend.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

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

}
