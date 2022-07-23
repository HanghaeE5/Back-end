package com.example.backend.notification.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "notification_content")
public class NotificationContent {
    @Id
    String id;
    String content;
    public NotificationContent(String content) {
        this.content = content;
    }

    public NotificationContent() {

    }
}