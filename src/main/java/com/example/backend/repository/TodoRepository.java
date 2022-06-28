package com.example.backend.repository;

import com.example.backend.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
    public Page<Todo> findAllByTodoDate(Pageable pageable);
    public Page<Todo> findByAllByCategory(Pageable pageable);
}
