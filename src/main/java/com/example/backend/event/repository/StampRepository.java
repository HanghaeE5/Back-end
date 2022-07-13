package com.example.backend.event.repository;

import com.example.backend.event.domain.Stamp;
import com.example.backend.user.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StampRepository extends JpaRepository<Stamp, Long> {

    @EntityGraph(attributePaths = {"stampDates"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Stamp> findByUser(User user);
}
