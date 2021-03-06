package com.example.backend.board.service;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.BoardTodo;
import com.example.backend.board.domain.Category;
import com.example.backend.board.dto.FilterEnum;
import com.example.backend.board.dto.SubEnum;
import com.example.backend.board.dto.condition.BoardSearchCondition;
import com.example.backend.board.dto.request.BoardTodoRequestDto;
import com.example.backend.board.dto.request.RequestDto;
import com.example.backend.board.dto.response.BoardResponseDto;
import com.example.backend.board.dto.response.PageBoardResponseDto;
import com.example.backend.board.repository.BoardRepository;
import com.example.backend.board.repository.BoardTodoRepository;
import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.chat.domain.Participant;
import com.example.backend.chat.redis.RedisRepository;
import com.example.backend.chat.repository.ChatRoomRepository;
import com.example.backend.chat.repository.ParticipantRepository;
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
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
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
    private final ChatRoomRepository chatRoomRepository;
    private final ParticipantRepository participantRepository;
    private final RedisRepository redisRepository;

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
            saveBoard.addParticipatingCount();
            ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(saveBoard.getTitle()));
            saveBoard.saveChatRoomId(chatRoom.getRoomId());
            participantRepository.save(new Participant(user, chatRoom));
            redisRepository.subscribe(chatRoom.getRoomId());
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
        }else if(sub.equals(SubEnum.content)) {
            if (Objects.equals(filter, FilterEnum.challenge)) {
                log.info("content, challenge search");
                boardPage = boardRepository.findByContentContainingAndCategory(keyword, Category.CHALLENGE, pageable);
            } else if (Objects.equals(filter, FilterEnum.daily)) {
                log.info("content, daily search");
                boardPage = boardRepository.findByContentContainingAndCategory(keyword, Category.DAILY, pageable);
            } else if (Objects.equals(filter, FilterEnum.my)) {
                log.info("content, my search");
                User user = getUser(email);
                boardPage = boardRepository.findByContentContainingAndUser(keyword, user, pageable);
            } else {
                log.info("content search");
                boardPage = boardRepository.findByContentContaining(keyword, pageable);
            }
//            else if(sub.equals(SubEnum.all)) {
//                boardPage = boardRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
//            }
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

    @Transactional
    public PageBoardResponseDto getBoardListV2(FilterEnum filter, String keyword, Pageable pageable, String email, SubEnum sub){

        BoardSearchCondition searchCondition = new BoardSearchCondition(sub, filter, keyword, email);

        Slice<Board> result = boardRepository.search(pageable, searchCondition);

        return new PageBoardResponseDto(
                BoardResponseDto.getDtoList(result.getContent()),
                result
        );
    }

    // ????????? ????????????
    @Transactional
    public BoardResponseDto getDetailBoard(Long id, String email) throws ParseException {
        User user = getUser(email);
        Board board = boardRepository.findById(id).orElseThrow(
                () -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        return new BoardResponseDto(board, todoRepository.existsByBoardAndUser(board, user), !withTodoExpiration(board));
    }

    @Transactional
    public void deleteBoard(Long id, String email) {
        Board board = getBoard(id);
        User user = getUser(email);
        if (!Objects.equals(board.getUser().getUserSeq(), user.getUserSeq())) {
            throw new CustomException(ErrorCode.INCORRECT_USERID);
        }
        if(board.getCategory().equals(Category.CHALLENGE)) {
            //???????????? ?????? ?????????
            throw new CustomException(ErrorCode.CHALLENGE_NOT_DELETE);
            //??????????????? ????????? ????????? TODO??? ????????? ???????????? ???????????????
        }
        if(board.getImageUrl().split(MsgEnum.IMAGE_DOMAIN.getMsg()).length >= 2 ){
            awsS3Service.deleteImage(board.getImageUrl().split(MsgEnum.IMAGE_DOMAIN.getMsg())[1]);
        }
        boardRepository.deleteById(id);
    }

    @Transactional
    public void updateBoard(Long id, String email, RequestDto requestDto) throws ParseException {
        Board board = getBoard(id);
        User user = getUser(email);
        if (!Objects.equals(board.getUser().getUserSeq(), user.getUserSeq())) {
            throw new CustomException(ErrorCode.INCORRECT_USERID);
        }
        //????????? ????????? ????????? ?????? ?????????
        if(board.getCategory().equals(Category.CHALLENGE)){
            throw new CustomException(ErrorCode.CHALLENGE_NOT_UPDATE);
            //??????????????? ????????? ????????? ???????????? TODO??? ????????? ?????? ???????????????
        }

        //Daily -> Challenge
        if (requestDto.getBoard().getCategory().equals(Category.CHALLENGE)){
            saveTodo(user, requestDto.getTodo(), board);
            board.addParticipatingCount();

            ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom(requestDto.getBoard().getTitle()));
            board.saveChatRoomId(chatRoom.getRoomId());
            participantRepository.save(new Participant(user, chatRoom));
        }
        
        //???????????? ?????? ????????? URL?????? ?????? ??? S3?????? ??????
        if(board.getImageUrl().split(MsgEnum.IMAGE_DOMAIN.getMsg()).length >= 2 &&
            !board.getImageUrl().equals(requestDto.getBoard().getImageUrl())
        ){
            awsS3Service.deleteImage(board.getImageUrl().split(MsgEnum.IMAGE_DOMAIN.getMsg())[1]);
        }

        board.update(requestDto.getBoard(), user);

    }

    @Transactional
    public void applyChallenge(Long boardId, String email) throws ParseException {
        User user = getUser(email);
        Board board = getBoard(boardId);

        //????????? ???????????? ???????????? ??????/????????? ??? ??????
        if(user.getUserSeq().equals(board.getUser().getUserSeq())){
            throw new CustomException(ErrorCode.CHALLENGE_CANCEL_AUTHOR_NOT);
        }

        //???????????? ?????? ?????????
        if (!board.getCategory().equals(Category.CHALLENGE)){
            throw new CustomException(ErrorCode.DAILY_NOY_APPLY_CHALLENGE);
        }
        //???????????? ?????? ?????????????????? ????????????
        //BoardTodo??? ????????? ?????? ???????????? ??????????????? ??? ????????? ?????? ??????
        if(!withTodoExpiration(board)){
            throw new CustomException(ErrorCode.CHALLENGE_CANCEL_APPLY_NOT);
        }


        //????????? TODO ??????
        List<Todo> todoList = new ArrayList<>();
        for (BoardTodo boardTodo : board.getBoardTodo()) {
            todoList.add(new Todo(boardTodo, user, board));
        }
        todoRepository.saveAll(todoList);
        board.addParticipatingCount();
    }

    @Transactional
    public void cancelChallenge(Long boardId, String email) throws ParseException {
        User user = getUser(email);
        Board board = getBoard(boardId);

        if(user.getUserSeq().equals(board.getUser().getUserSeq())){
            //????????? ???????????? ???????????? ????????? ??? ??????
            throw new CustomException(ErrorCode.CHALLENGE_CANCEL_AUTHOR_NOT);
        }

        //???????????? ?????? ?????????
        if (!board.getCategory().equals(Category.CHALLENGE)){
            throw new CustomException(ErrorCode.DAILY_NOY_APPLY_CHALLENGE);
        }

        //?????? ?????? ?????? ???????????? ?????? ?????????
        if (!todoRepository.existsByBoardAndUser(board, user)){
            throw new CustomException(ErrorCode.NOT_APPLY_CHALLENGE_NOT_CANCEL);
        }

        //???????????? ?????? ?????????????????? ?????? ??????
        //BoardTodo??? ????????? ?????? ???????????? ??????????????? ??? ????????? ?????? ??????
        if(!withTodoExpiration(board)){
            throw new CustomException(ErrorCode.CHALLENGE_CANCEL_APPLY_NOT);
        }

        //???????????? ????????? TODO ??????
        List<Todo> todoList = todoRepository.findAllByBoardAndUser(board, user);
        todoRepository.deleteAll(todoList);
        board.minusParticipatingCount();
    }

    @Transactional
    public void saveTodo(User user, BoardTodoRequestDto todo, Board board) throws ParseException {
        //????????? ????????? TODO ??? ??????
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

    private boolean withTodoExpiration(Board board) throws ParseException {
        for (BoardTodo boardTodo : board.getBoardTodo()) {
            String todayFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date(dateFormat.parse(todayFormat).getTime());
            if (boardTodo.getTodoDate().compareTo(today) < 0) {
                return false;
            }
        }
        return true;
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