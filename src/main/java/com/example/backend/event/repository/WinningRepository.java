package com.example.backend.event.repository;

import com.example.backend.event.domain.Winning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WinningRepository extends JpaRepository<Winning, Long> {
}
