package com.example.backend.user.repository;

import com.example.backend.user.domain.EmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailCheckRepository extends JpaRepository<EmailCheck, Long> {
    List<EmailCheck> findByEmailOrderByCreatedDateDesc(String email);
    void deleteByEmail(String email);
}
