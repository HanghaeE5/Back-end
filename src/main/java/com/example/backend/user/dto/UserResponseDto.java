package com.example.backend.user.dto;

import com.example.backend.user.domain.ProviderType;
import com.example.backend.user.domain.RoleType;
import com.example.backend.user.domain.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

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
    }
}
