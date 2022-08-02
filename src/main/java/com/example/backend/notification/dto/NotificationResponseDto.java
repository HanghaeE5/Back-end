package com.example.backend.notification.dto;

import com.example.backend.notification.domain.Notification;
import com.example.backend.notification.domain.Type;
import lombok.Getter;

@Getter
public class NotificationResponseDto {

    private Type type;
    private String message;
    private boolean readState;


    public NotificationResponseDto(Notification notification) {
        this.type = notification.getType();
        this.message = notification.getMessage();
        this.readState = notification.isReadState();
    }
}
