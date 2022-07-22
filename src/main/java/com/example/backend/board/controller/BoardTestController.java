package com.example.backend.board.controller;


import com.example.backend.board.dto.FilterEnum;
import com.example.backend.board.dto.SubEnum;
import com.example.backend.board.dto.response.PageBoardResponseDto;
import com.example.backend.board.service.BoardService;
import com.example.backend.user.common.LoadUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardTestController {

    private final BoardService boardService;

    @GetMapping("/test/board/v1")
    public ResponseEntity<PageBoardResponseDto> getBoardListV1(
            @RequestParam(required = false) FilterEnum filter,
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "all", required = false) SubEnum sub,
            @PageableDefault(sort="createdDate", direction= Sort.Direction.DESC) Pageable pageable
    ) {
        PageBoardResponseDto responseDto = boardService.getBoardList(
                filter, keyword, pageable, "a@a.com", sub
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @GetMapping("/test/board/v2")
    public ResponseEntity<PageBoardResponseDto> getBoardListV2(
            @RequestParam(required = false) FilterEnum filter,
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "all", required = false) SubEnum sub,
            @PageableDefault(sort="createdDate", direction= Sort.Direction.DESC) Pageable pageable
    ) {
        PageBoardResponseDto responseDto = boardService.getBoardListV2(
                filter, keyword, pageable, "a@a.com", sub
        );
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }
}
