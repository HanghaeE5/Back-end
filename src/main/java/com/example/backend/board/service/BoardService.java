package com.example.backend.board.service;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.Category;
import com.example.backend.board.dto.BoardRequestDto;
import com.example.backend.board.dto.BoardResponseDto;
import com.example.backend.board.repository.BoardRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.s3.AwsS3Service;
import com.example.backend.todo.dto.TodoRequestDto;
import com.example.backend.todo.service.TodoService;
import com.example.backend.user.common.LoadUser;
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
    public Page<BoardResponseDto> getBoardList(String filter, Integer page, Integer size, String sort) {

        Pageable pageable;
        if (sort == "asc")
            pageable = PageRequest.of(page, size, Sort.by("createdDate").ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Board> boardPage;


        if (Objects.equals(filter, "all")) {
            boardPage = boardRepository.findAll(pageable);
        } else
            boardPage = boardRepository.findAllByCategory(Category.CHALLENGE, pageable);
        return boardPage.map(BoardResponseDto::new);
    }
    // 게시물 상세조회
    @javax.transaction.Transactional
    public BoardResponseDto getDetailBoard(Long id) {
        isYours(id);
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return new BoardResponseDto(board);
    }
    // 게시글 작성 구현
    @Transactional
    public void save(String boardString, String todoString, MultipartFile file, String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow (
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );

        BoardRequestDto boardRequestDto = objectMapper.readValue(boardString, BoardRequestDto.class);
        TodoRequestDto todoRequestDto = null ;
        if (todoString != null) {
            todoRequestDto = objectMapper.readValue(todoString, TodoRequestDto.class);
        }

        Board board = Board.builder()
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .category(boardRequestDto.getCategory())
                .imageUrl(awsS3Service.uploadImage(file))
                .user(user)
                .build();
//        Board board = new Board(boardRequestDto, user);
        Board saved = boardRepository.save(board);

        if (todoRequestDto != null) {
            todoRequestDto.setBoardId(saved.getId());
            todoService.saveList(todoRequestDto, email);
        }
    }

    // 게시글 삭제
//    @Transactional
//    public void deleteBoard(Long id, MultipartFile file) {
//        // 이메일로 User 객체를 꺼내온다.
//        // id를 이용해 Board 객체를 꺼내온다.
//        // Board 객체 id, User 객체 id로 현재 사용자가 작성한 board인지 판별
//        isYours(id);
//        awsS3Service.deleteImage();
//        boardRepository.deleteById(id);
//    }
    // 게시글 수정
//    @Transactional
//    public void updateBoard(Long id, BoardRequestDto requestDto, String email) {
//        User user = userRepository.findByEmail(email).orElseThrow(
//                () -> new CustomException(ErrorCode.USER_NOT_FOUND));
//        Board board = isYours(id);
//        board.update(requestDto, user);
//    }
    // 게시글 검색
//    @Transactional
//    public Page<BoardResponseDto> searchBoard(String classify, String keyword, String filter, Integer page, Integer size, String sort) {
//        Pageable pageable;
//        if(sort == "asc") {
//            pageable = PageRequest.of(page, size, Sort.by("createdDate").ascending());
//        }
//        else {
//            pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
//        }
//        Page<Board> boardPage;
//        if(Objects.equals(classify, "title")) {
//            boardPage = boardRepository.findAllByTitleContaining(keyword, pageable);
//        }
//    }
//    @Transactional
//    public Page<BoardResponseDto> searchBoard(String classify, String keyword, String filter, Integer page, Integer size, String sort) {
//        Pageable pageable;
//        if(sort == "asc") {
//            pageable = PageRequest.of(page, size, Sort.by("createdDate").ascending());
//        }
//        else {
//            pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
//        }
//        Page<Board> boardPage;
//            boardPage = boardRepository.findAllByTitleContaining(keyword, pageable);
//        return boardPage.map(BoardResponseDto::new);
//
//    }
    // 게시글 id 와 게시글을 작성한 user의 id가 동일한지 확인

    private Board isYours(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.TODO_NOT_FOUND));
        if(!Objects.equals(board.getUser().getUserId(), LoadUser.getEmail())) {
            throw new CustomException(ErrorCode.INCORRECT_USERID);
        }
        return board;
    }
}
