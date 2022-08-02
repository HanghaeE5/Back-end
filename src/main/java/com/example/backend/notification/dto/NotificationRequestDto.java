package com.example.backend.notification.dto;

import com.example.backend.notification.domain.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto implements Serializable {
    private Type type;
    private String message;
}
