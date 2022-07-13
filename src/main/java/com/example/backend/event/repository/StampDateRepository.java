package com.example.backend.event.repository;

import com.example.backend.event.domain.Stamp;
import com.example.backend.event.domain.StampDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface StampDateRepository extends JpaRepository<StampDate, Long> {

    boolean existsByStampAndStampDate(Stamp stamp, Date stampDate);
}
