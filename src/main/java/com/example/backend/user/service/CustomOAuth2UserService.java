package com.example.backend.user.service;

import com.example.backend.event.domain.Stamp;
import com.example.backend.event.repository.StampRepository;
import com.example.backend.exception.OAuthProviderMissMatchException;
import com.example.backend.user.domain.ProviderType;
import com.example.backend.user.domain.RoleType;
import com.example.backend.user.domain.User;
import com.example.backend.user.domain.UserPrincipal;
import com.example.backend.user.oauth.info.OAuth2UserInfo;
import com.example.backend.user.oauth.info.OAuth2UserInfoFactory;
import com.example.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final StampRepository stampRepository;

    @Value("${basic.profile.img}")
    private String basicImg;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User user = super.loadUser(userRequest);

        try {
            return this.process(userRequest, user);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User process(OAuth2UserRequest userRequest, OAuth2User user) {
        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
        User savedUser;
        Optional<User> optionalUser = userRepository.findByEmail(userInfo.getEmail());
        //이미 가입된 회원
        if (optionalUser.isPresent()) {
            //추후에 providerType, userid -> List로 받을 수 있게 변경하기
            savedUser = optionalUser.get();
            savedUser.updateSocialId(userInfo.getId(), providerType);
        } else {
            //최초 가입
            savedUser = createUser(userInfo, providerType);
            stampRepository.save(new Stamp(savedUser));
        }

        return UserPrincipal.create(savedUser, user.getAttributes());
    }

    private User createUser(OAuth2UserInfo userInfo, ProviderType providerType) {
        String imageUrl = userInfo.getImageUrl() == null || userInfo.getImageUrl().isEmpty() ? basicImg : userInfo.getImageUrl();
        User user = new User(
                userInfo.getId(),
                userInfo.getEmail(),
                "Y",
                imageUrl,
                providerType,
                RoleType.USER
        );
        log.info("before saveAndFlush");
        return userRepository.saveAndFlush(user);
    }

}
