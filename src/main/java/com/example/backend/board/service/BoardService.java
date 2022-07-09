package com.example.backend.board.service;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.BoardTodo;
import com.example.backend.board.domain.Category;
import com.example.backend.board.dto.*;
import com.example.backend.board.repository.BoardRepository;
import com.example.backend.board.repository.BoardTodoRepository;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.msg.MsgEnum;
import com.example.backend.s3.AwsS3Service;
import com.example.backend.todo.domain.Todo;
import com.example.backend.todo.repository.TodoRepository;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;
    private final TodoRepository todoRepository;
    private final BoardTodoRepository boardTodoRepository;


    public String saveImage(MultipartFile file){
        if (file.isEmpty()){
            throw new CustomException(ErrorCode.FILE_NULL);
        }
        return awsS3Service.uploadImage(file);
    }

    @Transactional
    public void save(RequestDto requestDto, String email) throws Exception {
        User user = getUser(email);
        Board saveBoard = boardRepository.save(new Board(requestDto.getBoard(), user));
        if(requestDto.getBoard().getCategory().equals(Category.CHALLENGE)
                 && requestDto.getTodo() != null){
            saveTodo(user, requestDto.getTodo(), saveBoard);
        }
    }

    @Transactional
    public PageBoardResponseDto getBoardList(FilterEnum filter, String keyword, Pageable pageable, String email, SubEnum sub) {
        Page<Board> boardPage;
        if(sub.equals(SubEnum.title)){
            if(Objects.equals(filter, FilterEnum.challenge)) {
                log.info("title, challenge search");
                boardPage = boardRepository.findByTitleContainingAndCategory(keyword, Category.CHALLENGE, pageable);
            } else if(Objects.equals(filter, FilterEnum.daily)) {
                log.info("title, daily search");
                boardPage = boardRepository.findByTitleContainingAndCategory(keyword, Category.DAILY, pageable);
            } else if(Objects.equals(filter, FilterEnum.my)){
                log.info("title, my search");
                User user = getUser(email);
                boardPage = boardRepository.findByTitleContainingAndUser(keyword, user, pageable);
            }else{
                log.info("title search");
                boardPage = boardRepository.findByTitleContaining(keyword, pageable);
            }
        }else if(sub.equals(SubEnum.content)){
            if(Objects.equals(filter, FilterEnum.challenge)) {
                log.info("content, challenge search");
                boardPage = boardRepository.findByContentContainingAndCategory(keyword, Category.CHALLENGE, pageable);
            } else if(Objects.equals(filter, FilterEnum.daily)) {
                log.info("content, daily search");
                boardPage = boardRepository.findByContentContainingAndCategory(keyword, Category.DAILY, pageable);
            } else if(Objects.equals(filter, FilterEnum.my)){
                log.info("content, my search");
                User user = getUser(email);
                boardPage = boardRepository.findByContentContainingAndUser(keyword, user, pageable);
            }else{
                log.info("content search");
                boardPage = boardRepository.findByContentContaining(keyword, pageable);
            }
        }else{
            if(Objects.equals(filter, FilterEnum.challenge)) {
                log.info("challenge search");
                boardPage = boardRepository.findAllByCategory(Category.CHALLENGE, pageable);
            } else if(Objects.equals(filter, FilterEnum.daily)) {
                log.info("daily search");
                boardPage = boardRepository.findAllByCategory(Category.DAILY, pageable);
            } else if(Objects.equals(filter, FilterEnum.my)){
                log.info("my search");
                User user = getUser(email);
                boardPage = boardRepository.findByUser(user, pageable);
            }else{
                log.info("search");
                boardPage = boardRepository.findAll(pageable);
            }
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

    @Transactional
    public void deleteBoard(Long id, String email) {
        Board board = getBoard(id);
        User user = getUser(email);
        if (!Objects.equals(board.getUser().getUserSeq(), user.getUserSeq())) {
            throw new CustomException(ErrorCode.INCORRECT_USERID);
        }
        if(board.getCategory().equals(Category.CHALLENGE)) {
            //칠린지는 삭제 불가능
            throw new CustomException(ErrorCode.CHALLENGE_NOT_DELETE);
            //삭제하려면 신청한 사람의 TODO를 어떻게 처리할지 생각해야함
        }
        awsS3Service.deleteImage(board.getImageUrl().split(MsgEnum.IMAGE_DOMAIN.getMsg())[1]);
        boardRepository.deleteById(id);
    }

    @Transactional
    public void updateBoard(Long id, String email, RequestDto requestDto) throws ParseException {
        Board board = getBoard(id);
        User user = getUser(email);
        if (!Objects.equals(board.getUser().getUserSeq(), user.getUserSeq())) {
            throw new CustomException(ErrorCode.INCORRECT_USERID);
        }
        //기존에 첼린지 였으면 변경 불가능
        if(board.getCategory().equals(Category.CHALLENGE)){
            throw new CustomException(ErrorCode.CHALLENGE_NOT_UPDATE);
            //변경하려면 기존에 신청한 사람들의 TODO를 어떻게 할지 생각해야함
        }

        //Daily -> Challenge
        if (requestDto.getBoard().getCategory().equals(Category.CHALLENGE)){
            saveTodo(user, requestDto.getTodo(), board);
        }

        awsS3Service.deleteImage(board.getImageUrl().split(MsgEnum.IMAGE_DOMAIN.getMsg())[1]);
        board.update(requestDto.getBoard(), user);

    }

    @Transactional
    public void saveTodo(User user, BoardTodoRequestDto todo, Board board) throws ParseException {
        List<Todo> todoList = new ArrayList<>();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (String todoDate : todo.getTodoDateList()) {
            Date date = formatter.parse(todoDate);
            todoList.add(new Todo(todo, user, board, date));
        }
        todoRepository.saveAll(todoList);

        List<BoardTodo>  boardTodoList = new ArrayList<>();
        for (String todoDate : todo.getTodoDateList()) {
            Date date = formatter.parse(todoDate);
            boardTodoList.add(new BoardTodo(todo, date, board));
        }
        boardTodoRepository.saveAll(boardTodoList);
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