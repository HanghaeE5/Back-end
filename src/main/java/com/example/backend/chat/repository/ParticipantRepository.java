package com.example.backend.chat.repository;

import com.example.backend.chat.domain.Participant;
import com.example.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findAllByUser(User user);
}
