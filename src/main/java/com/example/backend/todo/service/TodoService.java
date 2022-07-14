package com.example.backend.todo.service;

import com.example.backend.board.repository.BoardRepository;
import com.example.backend.character.repository.CharacterRepository;
import com.example.backend.character.service.CharacterService;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.dto.request.TodoRequestDto;
import com.example.backend.todo.dto.request.TodoScopeRequestDto;
import com.example.backend.todo.dto.request.TodoUpdateRequestDto;
import com.example.backend.todo.dto.response.TodoDoneResponseDto;
import com.example.backend.todo.dto.response.TodoResponseDto;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final UserRepository userRepository;
    private final CharacterService characterService;

    @Transactional
    public Page<TodoResponseDto> getTodoList(String email, String filter, Integer page, Integer size, String sort) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        Pageable pageable;

        if (Objects.equals(sort, "asc")) {
            pageable = PageRequest.of(page, size, Sort.by("todoDate").ascending());
        } else if (Objects.equals(sort, "desc")){
            pageable = PageRequest.of(page, size, Sort.by("todoDate").descending());
        } else {
            throw new CustomException(ErrorCode.INVALID_SORTING_OPTION);
        }
        Page<Todo> todoPage;

        if (Objects.equals(filter, "all")) {
            todoPage = todoRepository.findAllTodo(pageable, user);
        } else if (Objects.equals(filter, "doingList")) {
            todoPage = todoRepository.findAllByTodoStateFalse(pageable, user);
        } else if (Objects.equals(filter, "doneList")){
            todoPage = todoRepository.findAllByTodoStateTrue(pageable, user);
        } else {
            throw new CustomException(ErrorCode.INVALID_FILTER_OPTION);
        }

        return todoPage.map(TodoResponseDto::new);

    }


    public void saveList(TodoRequestDto requestDto, String email) throws ParseException {

        List<Todo> todoList = new ArrayList<>();

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        for (String todoDate : requestDto.getTodoDateList()) {
            Date date = formatter.parse(todoDate);
            todoList.add(new Todo(requestDto, user, date));
        }

        todoRepository.saveAll(todoList);

    }


    @Transactional
    public TodoDoneResponseDto done(String email, Long id) {
        Todo todo = getTodo(id, email);
        if (todo.isState()) {
            throw new CustomException(ErrorCode.TODO_ALREADY_DONE);
        }
        todo.done();
        return characterService.upgrade(email, todo);
    }


    @Transactional
    public void update(TodoUpdateRequestDto requestDto, String email, Long id) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = formatter.parse(requestDto.getTodoDate());
        Todo todo = getTodo(id, email);
        if (todo.getBoard() != null) {
            throw new CustomException(ErrorCode.CHALLENGE_UPDATE_FORBIDDEN);
        }
        todo.update(requestDto, date);
    }


    public void deleteTodo(String email, Long id) {

        Todo todo = getTodo(id, email);
        if (todo.getBoard() != null) {
            throw new CustomException(ErrorCode.CHALLENGE_DELETE_FORBIDDEN);
        }
        todoRepository.deleteById(id);

    }


    private Todo getTodo(Long id, String email) {

        Todo todo = todoRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.TODO_NOT_FOUND)
        );
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        if (!Objects.equals(todo.getUser().getUserSeq(), user.getUserSeq())) {
            throw new CustomException(ErrorCode.INCORRECT_USERID);
        }
        return todo;

    }

    @Transactional
    public void updateScope(TodoScopeRequestDto requestDto, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
        user.updatePublicScope(requestDto.getPublicScope());
    }
}
