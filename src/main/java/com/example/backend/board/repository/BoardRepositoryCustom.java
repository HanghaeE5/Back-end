package com.example.backend.board.repository;

import com.example.backend.board.dto.response.BoardResponseDtoV2;
import org.springframework.data.domain.Page;

public interface BoardRepositoryCustom {


    Page<BoardResponseDtoV2> search(Page)

}
