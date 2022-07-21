package com.example.backend.board.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.Category;
import com.example.backend.board.dto.FilterEnum;
import com.example.backend.board.dto.SubEnum;
import com.example.backend.board.dto.condition.BoardSearchCondition;
import com.example.backend.user.domain.User;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
                .orderBy(boardSort(pageable))
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


    private OrderSpecifier<?> boardSort(Pageable page) {
        //서비스에서 보내준 Pageable 객체에 정렬조건 null 값 체크
        if (!page.getSort().isEmpty()) {
            //정렬값이 들어 있으면 for 사용하여 값을 가져온다
            for (Sort.Order order : page.getSort()) {
                // 서비스에서 넣어준 DESC or ASC 를 가져온다.
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                // 서비스에서 넣어준 정렬 조건을 스위치 케이스 문을 활용하여 셋팅하여 준다.
                switch (order.getProperty()){
                    case "title":
                        return new OrderSpecifier(direction, board.title);
                    case "content":
                        return new OrderSpecifier(direction, board.content);
                    case "createdDate":
                        return new OrderSpecifier(direction, board.createdDate);
                }
            }
        }
        return null;
    }
}
