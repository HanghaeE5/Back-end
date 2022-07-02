package com.example.backend.user.repository;

import com.example.backend.user.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, Long> {
    UserRefreshToken findByEmail(String email);
    UserRefreshToken findByEmailAndRefreshToken(String email, String refreshToken);
}
