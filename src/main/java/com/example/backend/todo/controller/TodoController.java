package com.example.backend.todo.controller;

import com.example.backend.todo.dto.TodoRequestDto;
import com.example.backend.todo.dto.TodoResponseDto;
import com.example.backend.user.security.UserDetailsImpl;
import com.example.backend.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;


@RestController
@RequiredArgsConstructor
public class TodoController {

    public final TodoService todoService;


    // 목록 조회
    @GetMapping("/todo")
    public ResponseEntity<Page<TodoResponseDto>> getTodoList(
            @RequestParam String filter,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Page<TodoResponseDto> responseDtoList = todoService.getTodoList(userDetails, filter, page, size, sort);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }


    // 목록 추가 완료
    // Datetime 리스트로 받아서 한번에 저장
    // boardId 유무에 따라 따로 저장
    @PostMapping("/todo")
    public ResponseEntity<String> postTodo(
            @RequestBody TodoRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws ParseException {
        todoService.saveList(requestDto, userDetails);
        return ResponseEntity.status(HttpStatus.OK).body("todo 를 추가했습니다");
    }


    // 목록 완료 완료
    // done() 함수 만들어서 state 변경
    @PostMapping("/todo/{id}")
    public ResponseEntity<String> doneTodo(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        todoService.done(userDetails, id);
        return ResponseEntity.status(HttpStatus.OK).body("todo 를 완료했습니다");
    }


    // 목록 수정 완료
    // update() 함수로 수정
    @PutMapping("/todo/{id}")
    public ResponseEntity<String> updateTodo(
            @PathVariable Integer id,
            @RequestBody TodoRequestDto requestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws ParseException {
        todoService.update(requestDto, userDetails, id);
        return ResponseEntity.status(HttpStatus.OK).body("todo 를 수정했습니다");
    }


    // 목록 삭제 완료
    // id 로 삭제
    @DeleteMapping("todo/{id}")
    public ResponseEntity<String> deleteTodo(
            @PathVariable Integer id,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        todoService.deleteTodo(userDetails, id);
        return ResponseEntity.status(HttpStatus.OK).body("todo 를 삭제했습니다");
    }

}
