package com.example.backend.todo.service;

import com.example.backend.board.domain.Board;
import com.example.backend.board.repository.BoardRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.domain.User;
import com.example.backend.todo.dto.TodoRequestDto;
import com.example.backend.todo.dto.TodoResponseDto;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;


    public Page<TodoResponseDto> getTodoList(UserDetailsImpl userDetails, String filter, Integer page, Integer size, String sort) {

        Pageable pageable;

        if (sort == "asc") {
            pageable = PageRequest.of(page, size, Sort.by("todoDate").ascending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("todoDate").descending());
        }
        Page<Todo> todoPage;

        if (Objects.equals(filter, "all")) {
            todoPage = todoRepository.findAllTodo(pageable);
        } else if (Objects.equals(filter, "doingList")) {
            todoPage = todoRepository.findAllByTodoStateTrue(pageable);
        } else {
            todoPage = todoRepository.findAllByTodoStateFalse(pageable);
        }

        return todoPage.map(TodoResponseDto::new);

    }


    public void saveList(TodoRequestDto requestDto, UserDetailsImpl userDetails) throws ParseException {

        List<Todo> todoList = new ArrayList<>();

        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        if (requestDto.getBoardId() != null) {
            Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow(
                    () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
            );
            for (String todoDate : requestDto.getTodoDateList()) {
                Date date = formatter.parse(todoDate);
                todoList.add(new Todo(requestDto, user, board, date));
            }
        }
        else {
            for (String todoDate : requestDto.getTodoDateList()) {
                Date date = formatter.parse(todoDate);
                todoList.add(new Todo(requestDto, user, date));
            }
        }
        todoRepository.saveAll(todoList);

    }


    @Transactional
    public void done(UserDetailsImpl userDetails, Integer id) {

        Todo todo = getTodo(id, userDetails);
        todo.done();

    }


    @Transactional
    public void update(TodoRequestDto requestDto, UserDetailsImpl userDetails, Integer id) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(requestDto.getTodoDate());
        Todo todo = getTodo(id, userDetails);
        todo.update(requestDto, date);

    }


    public void deleteTodo(UserDetailsImpl userDetails, Integer id) {

        getTodo(id, userDetails);
        todoRepository.deleteById(id);

    }


    private Todo getTodo(Integer id, UserDetailsImpl userDetails) {

        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.TODO_NOT_FOUND)
        );
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if (!Objects.equals(todo.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.INCORRECT_USERID);
        }
        return todo;

    }
}
