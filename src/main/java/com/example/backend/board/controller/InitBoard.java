package com.example.backend.board.controller;

import com.example.backend.board.domain.Board;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Component
@RequiredArgsConstructor
public class InitBoard {
    private final InitBoardService initBoardService;

    @PostConstruct
    public void init() {
        initBoardService.init();
    }
    @Component
    static class InitBoardService {
        @PersistenceContext
        EntityManager em;
        @Transactional
        public void init() {
            String qlString =
                    "select m from User m " +
                            "where m.email = :email";
            User findUser = em.createQuery(qlString, User.class)
                    .setParameter("email", "yung6532@naver.com")
                    .getSingleResult();


            Board board = new Board("제목", "내용", findUser);
            em.persist(board);
            for (int i = 0; i < 1000; i++) {
                em.persist(new Board("제목이라는 단어가 길면 속도 차이가 얼마나 되는지 비교해 보려고 한다" + i, "내용"+(1000-i), findUser));
            }
        }
    }
}