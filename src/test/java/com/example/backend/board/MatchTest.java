package com.example.backend.board;

import com.example.backend.board.domain.Board;
import com.example.backend.board.repository.BoardRepository;
import com.example.backend.user.repository.UserRepository;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.example.backend.board.domain.QBoard.board;

@SpringBootTest
@Transactional
public class MatchTest {

    @PersistenceContext
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
    }

    @Test
    public void matchTest(){
        NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                "function('match',{0},{1})", board.title, "+" + "나다" + "*");

        List<Board> content = queryFactory.
                select(board)
                .from(board)
                .where(booleanTemplate.gt(0))
                .fetch();

//        for (Board board1 : content) {
//            System.out.println("boardTitle = " + board1.getTitle());
//            System.out.println("boardContent = " + board1.getContent());
//        }

    }

    @Test
    public void matchTest2(){
        NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                "function('matchs',{0},{1},{2})", board.title, board.content, "+" + "미슐" + "*");

        List<Board> content = queryFactory.
                select(board)
                .from(board)
                .where(booleanTemplate.gt(0))
                .fetch();

//        for (Board board1 : content) {
//            System.out.println("boardTitle = " + board1.getTitle());
//            System.out.println("boardContent = " + board1.getContent());
//        }

    }
}
