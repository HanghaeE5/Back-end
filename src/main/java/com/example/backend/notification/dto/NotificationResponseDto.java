package com.example.backend.notification.dto;

import com.example.backend.notification.domain.Notification;
import com.example.backend.notification.domain.Type;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponseDto {

    private Type type;
    private String message;
    private boolean readState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;

    public NotificationResponseDto(Notification notification) {
        this.type = notification.getType();
        this.message = notification.getMessage();
        this.readState = notification.isReadState();
        this.createdDate = notification.getCreatedDate();
    }
}
