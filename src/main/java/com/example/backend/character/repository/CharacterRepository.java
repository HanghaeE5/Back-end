package com.example.backend.character.repository;

import com.example.backend.character.domain.Characters;
import com.example.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Characters, Long> {
}
