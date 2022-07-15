package com.example.backend.user.dto;

import com.example.backend.character.dto.CharacterResponseDto;
import com.example.backend.todo.dto.response.TodoResponseDto;
import com.example.backend.user.domain.ProviderType;
import com.example.backend.user.domain.PublicScope;
import com.example.backend.user.domain.RoleType;
import com.example.backend.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
public class UserResponseDto {
    private Long id;
    private String userId;
    private String nick;
    private String email;
    private String profileImageUrl;
    private ProviderType providerType;
    private RoleType roleType;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedDate;
    private CharacterResponseDto characterInfo;
    private PublicScope publicScope;
    private List<TodoResponseDto> todayTodoList = new ArrayList<>();

    public UserResponseDto(User user){
        this.id = user.getUserSeq();
        this.userId = user.getUserId();
        this.nick = user.getUsername();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfileImageUrl();
        this.providerType = user.getProviderType();
        this.roleType = user.getRoleType();
        this.createdDate = user.getCreatedDate();
        this.modifiedDate = user.getModifiedDate();
        this.publicScope = user.getPublicScope();
    }

    public UserResponseDto(User user, CharacterResponseDto characterResponseDto, List<TodoResponseDto> responseDtoList){
        this.id = user.getUserSeq();
        this.userId = user.getUserId();
        this.nick = user.getUsername();
        this.email = user.getEmail();
        this.profileImageUrl = user.getProfileImageUrl();
        this.providerType = user.getProviderType();
        this.roleType = user.getRoleType();
        this.createdDate = user.getCreatedDate();
        this.modifiedDate = user.getModifiedDate();
        this.characterInfo = characterResponseDto;
        this.publicScope = user.getPublicScope();
        this.todayTodoList = responseDtoList;
    }
}
