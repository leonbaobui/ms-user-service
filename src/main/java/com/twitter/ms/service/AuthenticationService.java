package com.twitter.ms.service;

import com.twitter.ms.exception.DataNotFoundException;
import com.twitter.ms.model.User;
import com.twitter.ms.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    public User getAuthenticatedUser(Long authUserId) {
        return userRepository.getUserById(authUserId, User.class)
                .orElseThrow(() -> new DataNotFoundException("userId", "Not authenticated", HttpStatus.BAD_REQUEST));
    }
}
