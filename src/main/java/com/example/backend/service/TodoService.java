package com.example.backend.service;

import com.example.backend.domain.Board;
import com.example.backend.domain.Todo;
import com.example.backend.dto.request.TodoRequestDto;
import com.example.backend.dto.response.TodoResponseDto;
import com.example.backend.repository.BoardRepository;
import com.example.backend.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final BoardRepository boardRepository;


    public Page<TodoResponseDto> getTodoList(UserDetailsImpl userDetails, String filter, Integer page, Integer size, String sort) {


        Pageable pageable = PageRequest.of(page, size, Sort.Direction.valueOf(sort));
        Page<Todo> todoPage;

        if (Objects.equals(filter, "all")) {
            // 일단 스크롤 먼저 하기 위해 넣어놓음
            todoPage = todoRepository.findAll(pageable);
        } else if (Objects.equals(filter, "doingList")) {
            // 일단 스크롤 먼저 하기 위해 넣어놓음
            todoPage = todoRepository.findAllByTodoDate(pageable);
        } else {
            // 일단 스크롤 먼저 하기 위해 넣어놓음
            todoPage = todoRepository.findByAllByCategory(pageable);
        }
        return todoPage.map(TodoResponseDto::new);
    }


    public void saveList(TodoRequestDto requestDto, UserDetailsImpl userDetails) {
        List<Todo> todoList = new ArrayList<>();
        if (requestDto.getBoardId() != null) {
            Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 게시글 존재하지 않음")
            );
            for (Date todoDate : requestDto.getTodoDateList()) {
                todoList.add(new Todo(requestDto, userDetails, board, todoDate));
            }
        }
        else {
            for (Date todoDate : requestDto.getTodoDateList()) {
                todoList.add(new Todo(requestDto, userDetails, todoDate));
            }
        }
        todoRepository.saveAll(todoList);
    }

    @Transactional
    public void done(UserDetailsImpl userDetails, Integer id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글 존재하지 않음")
        );
        if (todo.getUser().getId() != userDetails.getUser().getId()) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다");
        }
        todo.done();
    }

    @Transactional
    public void update(TodoRequestDto requestDto, UserDetailsImpl userDetails, Integer id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글 존재하지 않음")
        );
        if (todo.getUser().getId() != userDetails.getUser().getId()) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다");
        }
        todo.update(requestDto);
    }

    public void deleteTodo(UserDetailsImpl userDetails, Integer id) {
        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글 존재하지 않음")
        );
        if (todo.getUser().getId() != userDetails.getUser().getId()) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다");
        }
        todoRepository.deleteById(id);
    }

}
