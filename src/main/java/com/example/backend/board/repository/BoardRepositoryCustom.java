package com.example.backend.board.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.board.dto.condition.BoardSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {

    Page<Board> search(Pageable pageable, BoardSearchCondition searchCondition);

}
