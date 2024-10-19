package com.twitter.ms.security.oauth2;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.twitter.ms.dto.response.RegistrationResponse;
import com.twitter.ms.exception.RegistrationException;
import com.twitter.ms.mapper.UserMapper;
import com.twitter.ms.model.User;
import com.twitter.ms.repository.UserRepository;

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
        OAuth2User user = super.loadUser(userRequest);
        return new GoogleOAuth2User(user);
    }

    public void oauth2Register(OAuth2AuthenticationToken token, String idAttributeKey) {
        GoogleOAuth2User googleOAuth2User = (GoogleOAuth2User) token.getPrincipal();
        String email = googleOAuth2User.getEmail();
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User user = UserMapper.INSTANCE.oauth2RegistrationRequestToUserDAO(googleOAuth2User);
            userRepository.saveAndFlush(user);

            DefaultOAuth2User oAuth2User = new DefaultOAuth2User(
                    List.of(new SimpleGrantedAuthority(user.getRole())),
                    googleOAuth2User.getAttributes(),
                    idAttributeKey
                    );
            Authentication securityOAuth = new OAuth2AuthenticationToken(
                    oAuth2User,
                    List.of(new SimpleGrantedAuthority(user.getRole())),
                    token.getAuthorizedClientRegistrationId()
            );
            SecurityContextHolder.getContext().setAuthentication(securityOAuth);
        } else {
            throw new RegistrationException("Email", "Email is already registered", HttpStatus.FORBIDDEN);
        }
    }
}
