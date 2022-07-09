package com.example.backend.todo.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.todo.domain.Todo;
import com.example.backend.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("select t from Todo t where t.user=:user and ((t.todoDate<=CURRENT_DATE and t.state=true) or (t.state=false))")
    Page<Todo> findAllTodo(Pageable pageable, User user);

    @Query("select t from Todo t where t.user=:user and t.state=true")
    Page<Todo> findAllByTodoStateTrue(Pageable pageable, User user);

    @Query("select t from Todo t where t.user=:user and t.state=false")
    Page<Todo> findAllByTodoStateFalse(Pageable pageable, User user);

    List<Todo> findAllByBoardAndUser(Board board, User user);

    boolean existsByBoard(Board board);

    boolean existsByBoardAndUser(Board board, User user);

    @Query("select t from Todo t where t.user=:user and t.todoDate=CURRENT_DATE")
    List<Todo> findAllByTodoDate(User user);

}
