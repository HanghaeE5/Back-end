package com.example.backend.board.dto.condition;


import com.example.backend.board.dto.FilterEnum;
import com.example.backend.board.dto.SubEnum;
import com.example.backend.user.domain.User;
import lombok.Data;

@Data
public class BoardSearchCondition {

    private SubEnum sub;
    private FilterEnum filter;
    private String keyword;
    private User user;
}
