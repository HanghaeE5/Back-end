package com.example.backend.todo.repository;

import com.example.backend.board.domain.Board;

import java.util.List;

public interface TodoRepositoryCustom {

    long updateTodoByBoardIn(List<Board> boardList);
}
