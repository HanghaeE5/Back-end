package com.example.backend.friend.repository;

import com.example.backend.friend.domain.FriendRequest;
import com.example.backend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    List<FriendRequest> findAllByUserFromUserSeq(Long userSeq);

    List<FriendRequest> findAllByUserToUserSeq(Long userSeq);

    @Query("select f from FriendRequest f where f.userFrom=:user and f.userTo=:userFriend")
    Optional<FriendRequest> findRelation(User user, User userFriend);
}
