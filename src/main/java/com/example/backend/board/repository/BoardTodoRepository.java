package com.example.backend.board.repository;

import com.example.backend.board.domain.BoardTodo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardTodoRepository extends JpaRepository<BoardTodo, Long> {
}
