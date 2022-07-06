package com.example.backend.board.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.board.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"boardTodo"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Board> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"boardTodo"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<Board> findAllByCategory(Category category, Pageable pageable);
//    Page<Board> findAllByTitleContaining(String keyword, Pageable pageable);

}
