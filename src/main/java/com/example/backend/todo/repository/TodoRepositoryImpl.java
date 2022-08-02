package com.example.backend.todo.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.todo.domain.QTodo;
import com.example.backend.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.example.backend.todo.domain.QTodo.todo;


public class TodoRepositoryImpl implements TodoRepositoryCustom{
    @PersistenceContext
    private EntityManager em;

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public long updateTodoByBoardIn(List<Board> boardList) {


        long count = queryFactory
                .update(todo)
                .set(todo.board, new Board())
                .where(todo.board.in(boardList))
                .execute();
        //영속성 컨텍스트와 DB 데이터를 맞출 때
        em.flush();
        em.clear();

        return count;
    }
}
