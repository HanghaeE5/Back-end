package com.example.backend.board.service;

import com.example.backend.board.domain.Board;
import com.example.backend.board.dto.BoardRequestDto;
import com.example.backend.board.repository.BoardRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.s3.AwsS3Service;
import com.example.backend.todo.dto.TodoRequestDto;
import com.example.backend.todo.service.TodoService;
import com.example.backend.user.common.UserDetailsImpl;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final TodoService todoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional
    public void save(
            String boardString,
            String todoString,
            MultipartFile file,
            UserDetailsImpl userDetails
    ) throws ParseException, JsonProcessingException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        BoardRequestDto boardRequestDto = objectMapper.readValue(boardString, BoardRequestDto.class);
        TodoRequestDto todoRequestDto = objectMapper.readValue(todoString, TodoRequestDto.class);

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .category(boardRequestDto.getCategory())
                .imageUrl(awsS3Service.uploadImage(file))
                .user(user)
                .build();
        Board saved = boardRepository.save(board);

        if (todoRequestDto != null) {
            todoRequestDto.setBoardId(saved.getId());
            todoService.saveList(todoRequestDto, userDetails);
        }

    }

}
