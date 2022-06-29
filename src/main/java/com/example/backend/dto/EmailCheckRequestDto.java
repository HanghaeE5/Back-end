package com.example.backend.dto;

import lombok.Getter;

@Getter
public class EmailCheckRequestDto {

    private String email;
    private String code;
}
