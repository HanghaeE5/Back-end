package com.example.backend.board.domain;

import com.example.backend.board.dto.request.BoardRequestDto;
import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column
    private String imageUrl;

    @Column
    @ColumnDefault("0")
    private Long participatingCount;

    @Column
    private String chatRoomId;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    @BatchSize(size=100)
    private Set<BoardTodo> boardTodo = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    public Board(BoardRequestDto requestDto, User user) {
        this.category = requestDto.getCategory();
        this.user = user;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.imageUrl = requestDto.getImageUrl();
        this.participatingCount = 0L;
    }

    public void update(BoardRequestDto requestDto, User user){
        this.category = requestDto.getCategory();
        this.user = user;
        this.title = requestDto.getTitle();
        this.content = requestDto.getContent();
        this.imageUrl = requestDto.getImageUrl();
    }

    public void addParticipatingCount(){
        this.participatingCount = this.participatingCount + 1;
    }


    public void saveChatRoomId(String chatRoomId){
        this.chatRoomId = chatRoomId;
    }

    public void minusParticipatingCount() {
        this.participatingCount = this.participatingCount - 1;
    }
}
