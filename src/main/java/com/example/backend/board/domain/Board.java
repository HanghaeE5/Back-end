package com.example.backend.board.domain;

import com.example.backend.board.dto.BoardRequestDto;
import com.example.backend.common.BaseTime;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.common.UserDetailsImpl;
import com.example.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String category;

    @Column(nullable = true)
    private Long todoId;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "board")
    private List<Todo> todo;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Board(BoardRequestDto requestDto, UserDetailsImpl user) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.todoId = requestDto.getTodoId();
//        this.imageUrl = requestDto.getImage();
//        this.user = user.getNick();
    }
//    public Board(BoardRequestDto requestDto) {
//        this.title = requestDto.getTitle();
//        this.content = requestDto.getContent();
//        this.todoId = requestDto.getTodoId();
//        this.imageUrl = requestDto.getImage();
//        this.user = user;
//    }

    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.todoId = requestDto.getTodoId();
//        this.imageUrl = requestDto.getImage();
    }


//    연관관계 매핑 시 게시글 삭제와 함께 연관된 개개인의 todo-list 도 함께 삭제됨
//    @OneToMany(mappedBy = "board")
//    private List<Todo> todoList;

}