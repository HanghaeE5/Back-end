package com.example.backend.alarm.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public enum AlarmType {
    todoWith, chat, todoA, todoB, level, step,

    @JsonProperty("achieve")
    achieve
}