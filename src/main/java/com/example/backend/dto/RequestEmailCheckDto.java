package com.example.backend.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class RequestEmailCheckDto {

    private String email;
    private String code;
}
