package com.example.backend.chat.dto;

import com.example.backend.chat.domain.Participant;
import com.example.backend.user.dto.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponseDto {

    private UserResponseDto user;

    public ParticipantResponseDto(Participant participant) {
        System.out.println("==========================10");
        this.user = new UserResponseDto(participant.getUser());
        System.out.println("==========================11");
    }
}
