package com.example.backend.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequestDto implements Serializable {

    private String type;
    private Map<String, Object> body;
}
