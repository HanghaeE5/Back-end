package com.example.backend.repository;

import com.example.backend.domain.EmailCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailCheckRepository extends JpaRepository<EmailCheck, Long> {

    List<EmailCheck> findByEmailOrderByCreatedDateDesc(String email);

}
