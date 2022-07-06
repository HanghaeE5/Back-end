package com.example.backend.board.domain;

import com.example.backend.board.dto.BoardRequestDto;
import com.example.backend.common.domain.BaseTime;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    private String imageUrl;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Todo> todo;

    @OneToMany(mappedBy = "board", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<BoardTodo> boardTodo = new LinkedHashSet<>();

    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false)
    private User user;

    public Board(BoardRequestDto requestDto, User user, String imageUrl) {
        this.category = Category.valueOf(requestDto.getCategory());
        this.content = requestDto.getContent();
        this.title = requestDto.getTitle();
        this.user = user;
        this.imageUrl = imageUrl;
    }

//    public void update(BoardRequestDto requestDto, User user) {
//        this.title = requestDto.getTitle();
//        this.content = requestDto.getContent();
//        this.todoId = requestDto.getTodoId();
//        this.category = requestDto.getCategory();
//        this.user = user;
//        this.imageUrl = requestDto.getImage();
//    }


//    연관관계 매핑 시 게시글 삭제와 함께 연관된 개개인의 todo-list 도 함께 삭제됨
//    @OneToMany(mappedBy = "board")
//    private List<Todo> todoList;

}
