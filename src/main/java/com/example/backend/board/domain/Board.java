package com.example.backend.board.domain;

import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String category;

    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn
    private User user;

//    연관관계 매핑 시 게시글 삭제와 함께 연관된 개개인의 todo-list 도 함께 삭제됨
//    @OneToMany(mappedBy = "board")
//    private List<Todo> todoList;

}