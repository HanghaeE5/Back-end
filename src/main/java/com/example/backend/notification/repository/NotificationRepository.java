package com.example.backend.notification.repository;

import com.example.backend.notification.domain.Notification;
import com.example.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUser(User user);
}
