package com.example.backend.friend.repository;

import com.example.backend.friend.domain.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findAllByUserFromUserSeq(Long userSeq);
}
