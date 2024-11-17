package com.twitter.ms.security.oauth2;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.twitter.ms.dto.response.RegistrationResponse;
import com.twitter.ms.enums.AuthenticationProvider;
import com.twitter.ms.exception.RegistrationException;
import com.twitter.ms.mapper.UserMapper;
import com.twitter.ms.model.User;
import com.twitter.ms.repository.UserRepository;
import com.twitter.ms.utils.UserPrincipal;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoogleOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        return processGoogleOAuth2User(userRequest, oAuth2User);
    }

    private OAuth2User processGoogleOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        GoogleOAuth2User googleOAuth2User = new GoogleOAuth2User(oAuth2User);
        String email = googleOAuth2User.getEmail();
        Optional<User> existingUser = userRepository.findByEmail(email);
        User user = existingUser.map(existedUser -> updateExistingUser(existedUser, googleOAuth2User, userRequest))
                .orElseGet(() -> registerNewUser(googleOAuth2User));
        return googleOAuth2User;
//        return UserPrincipal.create(user, googleOAuth2User.getAttributes());
    }

    private User registerNewUser(GoogleOAuth2User googleOAuth2User) {
        User user = UserMapper.INSTANCE.oauth2RegistrationRequestToUserDAO(googleOAuth2User);
        user.setAuthenticationProvider(AuthenticationProvider.GOOGLE);
        return userRepository.saveAndFlush(user);
    }

    private User updateExistingUser(User existingUser, GoogleOAuth2User googleOAuth2User, OAuth2UserRequest oAuth2UserRequest) {
        if(!StringUtils.equalsIgnoreCase(oAuth2UserRequest.getClientRegistration().getRegistrationId(), String.valueOf(existingUser.getAuthenticationProvider()))) {
            throw new RegistrationException("auth_provider", "Looks like you're signed up with " +
                    existingUser.getAuthenticationProvider() + " account. Please use your " + existingUser.getAuthenticationProvider() +
                    " account to login.", HttpStatus.BAD_REQUEST);
        }
        existingUser.setFullName(googleOAuth2User.getName());
        existingUser.setAvatar(googleOAuth2User.getAvatarUrl());
        return userRepository.saveAndFlush(existingUser);
    }
}
