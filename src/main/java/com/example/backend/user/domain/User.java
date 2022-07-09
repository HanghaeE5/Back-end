package com.example.backend.user.domain;

import com.example.backend.chat.domain.Participant;
import com.example.backend.common.domain.BaseTime;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "USER")
public class User extends BaseTime {
    @JsonIgnore
    @Id
    @Column(name = "USER_SEQ")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSeq;

    @Column(name = "USER_ID", length = 64)
    @Size(max = 64)
    private String userId;

    @Column(name = "USERNAME", length = 100)
    @Size(max = 100)
    private String username;

    @JsonIgnore
    @Column(name = "PASSWORD", length = 128)
    @NotNull
    @Size(max = 128)
    private String password;

    @Column(name = "EMAIL", length = 512, unique = true)
    @NotNull
    @Size(max = 512)
    private String email;

    @Column(name = "EMAIL_VERIFIED_YN", length = 1)
    @NotNull
    @Size(min = 1, max = 1)
    private String emailVerifiedYn;

    @Column(name = "PROFILE_IMAGE_URL", length = 512)
    @NotNull
    @Size(max = 512)
    private String profileImageUrl;

    @Column(name = "PROVIDER_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProviderType providerType;

    @Column(name = "ROLE_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoleType roleType;

    @Column
    @Enumerated(EnumType.STRING)
    private PublicScope publicScope;

    @OneToMany(mappedBy = "user" ,fetch = FetchType.LAZY)
    private List<Participant> participantList;

    public User(
            @Size(max = 64) String userId,
            @NotNull @Size(max = 512) String email,
            @NotNull @Size(max = 1) String emailVerifiedYn,
            @NotNull @Size(max = 512) String profileImageUrl,
            @NotNull ProviderType providerType,
            @NotNull RoleType roleType
    ) {
        this.userId = userId;
        this.password = "NO_PASS";
        this.email = email != null ? email : "NO_EMAIL";
        this.emailVerifiedYn = emailVerifiedYn;
        this.profileImageUrl = profileImageUrl != null ? profileImageUrl : "";
        this.providerType = providerType;
        this.roleType = roleType;
    }

    @Builder
    public User(
            @Size(max = 100) String username,
            @NotNull String password,
            @NotNull @Size(max = 512) String email,
            @NotNull ProviderType providerType,
            @NotNull RoleType roleType
    ) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.emailVerifiedYn = "Y";
        this.profileImageUrl = "";
        this.providerType = providerType;
        this.roleType = roleType;
        this.userId = email;
    }

    public void updateSocialId(
            @Size(max = 64) String userId,
            @NotNull ProviderType providerType
            )
    {
        this.userId = userId;
        this.providerType = providerType;
    }

    public void updateProfileImage(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }

    public void updateNick(String username){
        this.username = username;
    }

    public void addNick(String username){
        this.username = username;
    }

    public void updatePassword(String password){
        this.password = password;
    }

    public void updatePublicScope(String publicScope) {
        this.publicScope = PublicScope.valueOf(publicScope);
    }

    @PrePersist
    public void prePersist() {
        this.publicScope = this.publicScope == null ? PublicScope.ALL : this.publicScope;
    }


}
