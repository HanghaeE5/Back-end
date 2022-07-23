package com.example.backend.board.repository;

import com.example.backend.board.domain.Board;
import com.example.backend.board.dto.condition.BoardSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface BoardRepositoryCustom {

    Slice<Board> search(Pageable pageable, BoardSearchCondition searchCondition);

}
