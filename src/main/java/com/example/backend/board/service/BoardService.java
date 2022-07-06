package com.example.backend.board.service;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.BoardTodo;
import com.example.backend.board.domain.Category;
import com.example.backend.board.dto.BoardRequestDto;
import com.example.backend.board.dto.BoardResponseDto;
import com.example.backend.board.dto.PageBoardResponseDto;
import com.example.backend.board.repository.BoardRepository;
import com.example.backend.board.repository.BoardTodoRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.s3.AwsS3Service;
import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.dto.TodoRequestDto;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.todo.service.TodoService;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final TodoService todoService;
    private final TodoRepository todoRepository;
    private final BoardTodoRepository boardTodoRepository;

    // 전체 게시글 목록 조회 구현
    public PageBoardResponseDto getBoardList(String filter, Integer page, Integer size, String sort) {
        Pageable pageable;

        if (sort == "asc")
            pageable = PageRequest.of(page, size, Sort.by("createdDate").ascending());
        else
            pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());

        Page<Board> boardPage;

        if(Objects.equals(filter, "challenge")) {
            boardPage = boardRepository.findAllByCategory(Category.CHALLENGE, pageable);
        } else if(Objects.equals(filter, "daily")) {
            boardPage = boardRepository.findAllByCategory(Category.DAILY, pageable);
        } else {
            boardPage = boardRepository.findAll(pageable);
        }

        return new PageBoardResponseDto(
                BoardResponseDto.getDtoList(boardPage.getContent()),
                boardPage
        );
    }

    // 게시물 상세조회
    @Transactional
    public BoardResponseDto getDetailBoard(Long id) {
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        return new BoardResponseDto(board);
    }
    // 게시글 작성 구현
    @Transactional
    public void save(BoardRequestDto board, TodoRequestDto todo, MultipartFile file, String email) throws Exception {
        User user = getUser(email);
        Board saveBoard = boardRepository.save(new Board(board, user, awsS3Service.uploadImage(file)));
        if(board.getCategory().equals(Category.CHALLENGE.toString()) && todo != null){
            todo.setBoardId(saveBoard.getId());
            todoService.saveList(todo, email);

            List<BoardTodo>  boardTodoList = new ArrayList<>();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (String todoDate : todo.getTodoDateList()) {
                Date date = formatter.parse(todoDate);
                boardTodoList.add(new BoardTodo(todo, date, saveBoard));
            }
            boardTodoRepository.saveAll(boardTodoList);
        }
    }

    // 게시글 삭제, BoardTodo, 사진
    //1. TODO에 board_id null
    //2. 사진 삭제
    //3. 게시글 삭제
    @Transactional
    public void deleteBoard(Long id, String email) {

        Board board = isYours(id, email);
        if(board.getCategory().equals(Category.CHALLENGE)) {
            List<Todo> todoList = todoRepository.findAllByBoard(board);
            for (Todo todo : todoList) {
                todo.changeNull();
            }
        }
        // 2. 사진 삭제
        awsS3Service.deleteImage(board.getImageUrl().split("ohnigabucket.s3.ap-northeast-2.amazonaws.com/")[1]);
        // 3. 게시글 삭제
        boardRepository.deleteById(id);
    }
//     게시글 수정
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

    // 게시글 작성자의 id와 User의 id가 동일한지 확인하는 메서드
    private Board isYours(Long id, String email) {
        Board board = getBoard(id);
        User user = getUser(email);
        if (!Objects.equals(board.getUser().getUserSeq(), user.getUserSeq())) {
            throw new CustomException(ErrorCode.INCORRECT_USERID);
        }
        return board;
    }

    private Board getBoard(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND)
        );
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }
}