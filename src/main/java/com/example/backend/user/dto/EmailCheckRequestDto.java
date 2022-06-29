package com.example.backend.user.dto;

import lombok.Getter;

@Getter
public class EmailCheckRequestDto {

    private String email;
    private String code;
}
