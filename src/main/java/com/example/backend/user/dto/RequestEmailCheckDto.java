package com.example.backend.user.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class RequestEmailCheckDto {

    private String email;
    private String code;
}
