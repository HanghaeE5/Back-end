package com.example.backend.todo.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    @Query("select t from Todo t where t.user=:user and ((t.todoDate>=CURRENT_DATE and t.state=true) or (t.state=false))")
    Page<Todo> findAllTodo(Pageable pageable, User user);

    @Query("select t from Todo t where t.user=:user and ((t.todoDate=CURRENT_DATE and t.state=false) or (t.todoDate=CURRENT_DATE and t.state=true))")
    Page<Todo> findAllTodayTodo(Pageable pageable, User user);

    @Query("select t from Todo t where t.user=:user and t.state=true")
    Page<Todo> findAllByTodoStateTrue(Pageable pageable, User user);

    @Query("select t from Todo t where t.user=:user and t.state=false")
    Page<Todo> findAllByTodoStateFalse(Pageable pageable, User user);

    List<Todo> findAllByBoardAndUser(Board board, User user);

    List<Todo> findByBoardIn(List<Board> board);

    boolean existsByBoardAndUser(Board board, User user);

    @Query("select t from Todo t where t.user=:user and t.todoDate=CURRENT_DATE")
    List<Todo> findAllByTodoDate(User user);

    List<Todo> findByUserAndTodoDate(User user, Date yesterday);

    @Query("select t from Todo t where t.user=:user and t.completeDate>=CURRENT_DATE")
    List<Todo> findTodayDone(User user);

//    @Query("select t from Todo t where t.user in :users and t.todoDate=:yesterday")
//    List<Todo> findAllByUsersAndTodoDate(List<User> users, Date yesterday);

}
