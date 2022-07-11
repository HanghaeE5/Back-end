package com.example.backend.todo.controller;

import com.example.backend.msg.MsgEnum;
import com.example.backend.todo.dto.request.TodoRequestDto;
import com.example.backend.todo.dto.response.TodoDoneResponseDto;
import com.example.backend.todo.dto.response.TodoResponseDto;
import com.example.backend.todo.dto.request.TodoUpdateRequestDto;
import com.example.backend.todo.dto.response.TodoResponseDto;
import com.example.backend.todo.dto.request.TodoScopeRequestDto;
import com.example.backend.todo.service.TodoService;
import com.example.backend.user.common.LoadUser;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;


@RestController
@RequiredArgsConstructor
public class TodoController {

    public final TodoService todoService;


    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "Todo 목록 조회")
    @GetMapping("/todo")
    public ResponseEntity<Page<TodoResponseDto>> getTodoList(
            @RequestParam String filter,
            @RequestParam Integer page,
            @RequestParam Integer size,
            @RequestParam String sort
    ) {
        LoadUser.loginAndNickCheck();
        Page<TodoResponseDto> responseDtoList = todoService.getTodoList(LoadUser.getEmail(), filter, page, size, sort);
        return ResponseEntity.status(HttpStatus.OK).body(responseDtoList);
    }


    // Datetime 리스트로 받아서 한번에 저장
    // boardId 유무에 따라 따로 저장
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "Todo 목록 추가")
    @PostMapping("/todo")
    public ResponseEntity<String> postTodo(
            @RequestBody TodoRequestDto requestDto
    ) throws ParseException {
        LoadUser.loginAndNickCheck();
        todoService.saveList(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.TODO_SAVE_SUCCESS.getMsg());
    }


    // done() 함수 만들어서 state 변경 + 캐릭터 경험치 up
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "Todo 완료")
    @PostMapping("/todo/{id}")
    public ResponseEntity<TodoDoneResponseDto> doneTodo(
            @PathVariable Long id
    ) {
        LoadUser.loginAndNickCheck();
        TodoDoneResponseDto responseDto = todoService.done(LoadUser.getEmail(), id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }


    // update() 함수로 수정
    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "Todo 목록 수정")
    @PutMapping("/todo/{id}")
    public ResponseEntity<String> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoUpdateRequestDto requestDto
    ) throws ParseException {
        LoadUser.loginAndNickCheck();
        todoService.update(requestDto, LoadUser.getEmail(), id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.TODO_UPDATE_SUCCESS.getMsg());
    }

    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "Todo 공개범위 설정")
    @PutMapping("todo/scope")
    public ResponseEntity<String> updateScope(
            @RequestBody TodoScopeRequestDto requestDto
    ) {
        LoadUser.loginAndNickCheck();
        todoService.updateScope(requestDto, LoadUser.getEmail());
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.TODO_SCOPE_CHANGED.getMsg());
    }


    @ApiImplicitParam(name = "Authorization", value = "Access Token", required = true, allowEmptyValue = false, paramType = "header", dataTypeClass = String.class, example = "access_token")
    @ApiOperation(value = "Todo 목록 삭제")
    @DeleteMapping("todo/{id}")
    public ResponseEntity<String> deleteTodo(
            @PathVariable Long id
    ) {
        LoadUser.loginAndNickCheck();
        todoService.deleteTodo(LoadUser.getEmail(), id);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(MsgEnum.TODO_DELETE_SUCCESS.getMsg());
    }

}
