package com.example.backend.todo.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.todo.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    @Query("select t from Todo t where (t.todoDate>=CURRENT_DATE and t.state=true) or (t.state=false)")
    Page<Todo> findAllTodo(Pageable pageable);

    @Query("select t from Todo t where t.state=true")
    Page<Todo> findAllByTodoStateTrue(Pageable pageable);

    @Query("select t from Todo t where t.state=false")
    Page<Todo> findAllByTodoStateFalse(Pageable pageable);

    List<Todo> findAllByBoard(Board board);

}
