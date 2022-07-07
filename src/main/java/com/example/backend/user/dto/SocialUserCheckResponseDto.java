package com.example.backend.user.dto;

import lombok.Getter;

@Getter
public class SocialUserCheckResponseDto {

    private String msg;
    private boolean socialUser;

    public SocialUserCheckResponseDto(String msg, boolean socialUser){
        this.msg = msg;
        this.socialUser = socialUser;
    }
}
