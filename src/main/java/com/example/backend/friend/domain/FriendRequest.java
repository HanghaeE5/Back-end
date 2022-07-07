package com.example.backend.friend.domain;

import com.example.backend.common.domain.BaseTime;
import com.example.backend.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class FriendRequest extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private boolean state;

    @ManyToOne
    @JoinColumn
    private User userFrom;

    @ManyToOne
    @JoinColumn
    private User userTo;

    public FriendRequest(User userFrom, User userTo) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.state = false;
    }

    public FriendRequest(User userFrom, User userTo, boolean state) {
        this.userFrom = userFrom;
        this.userTo = userTo;
        this.state = true;
    }

    public void linked() {
        this.state = !this.state;
    }
}
