package com.example.backend.board.service;

import com.example.backend.board.domain.Board;
import com.example.backend.board.dto.BoardRequestDto;
import com.example.backend.board.dto.BoardResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final TodoService todoService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    // 전체 게시글 목록 조회 구현
    @javax.transaction.Transactional
    public Page<BoardResponseDto> getBoardList(String filter, Integer page, Integer size, String sort, UserDetailsImpl userDetails) {

        Pageable pageable;
        if (sort == "asc")
            pageable = PageRequest.of(page, size, Sort.by("createdDate").ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Board> boardPage;


        if (Objects.equals(filter, "all")) {
            boardPage = boardRepository.findAll(pageable);
        } else
            boardPage = boardRepository.findAll(pageable);

        return boardPage.map(BoardResponseDto::new);
    }
    // 게시물 상세조회
    @javax.transaction.Transactional
    public BoardResponseDto getDetailBoard(Long id, UserDetailsImpl userDetails) {
        isYours(id, userDetails);
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다."));
        return new BoardResponseDto(board);
    }
    // 게시글 작성 구현
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

    // 게시글 삭제
    @javax.transaction.Transactional
    public void deleteBoard(Long id, UserDetailsImpl userDetails) {
        isYours(id, userDetails);
        boardRepository.deleteById(id);
    }
    // 게시글 수정
    @javax.transaction.Transactional
    public void updateBoard(Long id, BoardRequestDto requestDto, UserDetailsImpl userDetails) {
        Board board = isYours(id, userDetails);
        board.update(requestDto);
    }
    // 게시글 검색

//    @Transactional
//    public Page<BoardResponseDto> searchBoard(String classify, String keyword, String filter, Integer page, Integer size, String sort, UserDetailsImpl userDetails) {
//        Pageable pageable;
//        if(sort == "asc") {
//            pageable = PageRequest.of(page, size, Sort.by("createdDate").ascending());
//        }
//        else {
//            pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
//        }
//        Page<Board> boardPage;
//        if(Objects.equals(classify, "title")) {
//            boardPage = boardRepository.findAllByTitleContaining(keyword);
//        }
//    }
    // 게시글 id 와 게시글을 작성한 user의 id가 동일한지 확인

    private Board isYours(Long id, UserDetailsImpl userDetails) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        if(!Objects.equals(board.getUser().getUserId(), user.getUserId())) {
            throw new IllegalArgumentException("게시글 작성자가 아닙니다.");
        }
        return board;
    }
}
