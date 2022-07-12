package com.example.backend.chat.repository;

import com.example.backend.chat.domain.ChatRoom;
import com.example.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
}
