package com.example.backend.board.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.Category;
import com.example.backend.board.dto.FilterEnum;
import com.example.backend.board.dto.SubEnum;
import com.example.backend.board.dto.condition.BoardSearchCondition;
import com.example.backend.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.backend.board.domain.QBoard.board;
import static org.thymeleaf.util.StringUtils.isEmpty;

public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public BoardRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> search(Pageable pageable, BoardSearchCondition searchCondition) {

        List<Board> content = queryFactory.
                selectFrom(board)
                .where(
                        filterEq(searchCondition.getFilter(), searchCondition.getUser()),
                        keywordAndSubject(searchCondition.getKeyword(), searchCondition.getSub())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Board> countQuery = queryFactory
                .select(board)
                .from(board)
                .where(
                        filterEq(searchCondition.getFilter(), searchCondition.getUser()),
                        keywordAndSubject(searchCondition.getKeyword(), searchCondition.getSub())
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }


    private BooleanExpression filterEq(FilterEnum filterEnum, User user) {
        if (filterEnum == null){
            return null;
        }else if(filterEnum.my.equals(filterEnum)){
            return board.user.eq(user);
        }else{
            return board.category.eq(Category.valueOf(filterEnum.name().toUpperCase()));
        }
    }

    private BooleanExpression keywordAndSubject(String keyword, SubEnum subEnum){
        if (subEnum == null){
            return null;
        }else if (isEmpty(keyword)){
            return null;
        }else if(subEnum.title.equals(subEnum)){
            NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                    "function('match',{0},{1})", board.title, "+" + keyword + "*");
            return booleanTemplate.gt(0);
        }else if(subEnum.content.equals(subEnum)){
            NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                    "function('match',{0},{1})", board.content, "+" + keyword + "*");
            return booleanTemplate.gt(0);
        }else{
            NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                    "function('matchs',{0},{1},{2})", board.title, board.content, "+" + keyword + "*");
            return booleanTemplate.gt(0);
        }
    }
}
