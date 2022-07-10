package com.example.backend.chat.dto.response;

import com.example.backend.chat.domain.Participant;
import com.example.backend.user.dto.UserResponseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantResponseDto {

    private UserResponseDto user;

    public ParticipantResponseDto(Participant participant) {
        this.user = new UserResponseDto(participant.getUser());
    }
}
