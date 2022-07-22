package com.example.backend.board.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.Category;
import com.example.backend.board.dto.FilterEnum;
import com.example.backend.board.dto.SubEnum;
import com.example.backend.board.dto.condition.BoardSearchCondition;
import com.example.backend.exception.CustomException;
import com.example.backend.exception.ErrorCode;
import com.example.backend.user.domain.User;
import com.example.backend.user.repository.UserRepository;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.example.backend.board.domain.QBoard.board;
import static org.thymeleaf.util.StringUtils.isEmpty;

public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private final UserRepository userRepository;

    public BoardRepositoryImpl(EntityManager em, UserRepository userRepository) {
        this.queryFactory = new JPAQueryFactory(em);
        this.userRepository = userRepository;
    }

    @Override
    public Page<Board> search(Pageable pageable, BoardSearchCondition searchCondition) {

        List<Board> content = queryFactory.
                selectFrom(board)
                .where(
                        filterEq(searchCondition.getFilter(), searchCondition.getEmail()),
                        keywordAndSubject(searchCondition.getKeyword(), searchCondition.getSub())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(
                        boardSort(pageable, searchCondition.getKeyword(), searchCondition.getSub())
                            .stream().toArray(OrderSpecifier[]::new)
                        )
                .fetch();

        JPAQuery<Board> countQuery = queryFactory
                .select(board)
                .from(board)
                .where(
                        filterEq(searchCondition.getFilter(), searchCondition.getEmail()),
                        keywordAndSubject(searchCondition.getKeyword(), searchCondition.getSub())
                );

        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    private BooleanExpression filterEq(FilterEnum filterEnum, String email) {
        if (filterEnum == null){
            return null;
        }else if(filterEnum.my.equals(filterEnum)){
            User user = getUser(email);
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
                    "function('match',{0},{1})", board.title, "" + keyword + "*");
            return booleanTemplate.gt(0);
        }else if(subEnum.content.equals(subEnum)){
            NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                    "function('match',{0},{1})", board.content, "" + keyword + "*");
            return booleanTemplate.gt(0);
        }else{
            NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                    "function('matchs',{0},{1},{2})", board.title, board.content, "" + keyword + "*");
            return booleanTemplate.gt(0);
        }
    }

    private List<OrderSpecifier<?>> boardSort(Pageable page, String keyword, SubEnum subEnum) {
        List<OrderSpecifier<?>> orderSpecifierList = new ArrayList<>();

        OrderSpecifier<?> matchOrder = keywordAndSubjectSort(keyword, subEnum);
        if (matchOrder != null){
            orderSpecifierList.add(matchOrder);
        }
        orderSpecifierList.add(new OrderSpecifier(Order.DESC, board.createdDate));

//        if (!page.getSort().isEmpty()) {
//            for (Sort.Order order : page.getSort()) {
//                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
//                switch (order.getProperty()){
//                    case "title":
//                        orderSpecifierList.add(new OrderSpecifier(direction, board.title));
//                    case "content":
//                        orderSpecifierList.add(new OrderSpecifier(direction, board.content));
//                    default:
//                        orderSpecifierList.add(new OrderSpecifier(direction, board.createdDate));
//                }
//            }
//        }
        return orderSpecifierList;
    }

    private OrderSpecifier<?> keywordAndSubjectSort(String keyword, SubEnum subEnum){
        if (subEnum == null){
            return null;
        }else if (isEmpty(keyword)){
            return null;
        }else {
            Order direction = Order.DESC;
            NumberTemplate booleanTemplate;
            if(subEnum.title.equals(subEnum)){
                booleanTemplate = Expressions.numberTemplate(Double.class,
                        "function('match',{0},{1})", board.title, "" + keyword + "*");
            }else if(subEnum.content.equals(subEnum)){
                booleanTemplate = Expressions.numberTemplate(Double.class,
                        "function('match',{0},{1})", board.content, "" + keyword + "*");
            }else{
                booleanTemplate = Expressions.numberTemplate(Double.class,
                        "function('matchs',{0},{1},{2})", board.title, board.content, "" + keyword + "*");
            }
            return new OrderSpecifier(direction, booleanTemplate);
        }
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }
}
