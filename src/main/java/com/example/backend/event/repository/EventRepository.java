package com.example.backend.event.repository;

import com.example.backend.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByRunTime(Date runTime);

}
