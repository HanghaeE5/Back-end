package com.example.backend.notification.domain;

import lombok.Getter;

@Getter
public class Review {

    Long id;
    String review;

    public Review(Long id, String review){
        this.id = id;
        this.review = review;
    }
}
