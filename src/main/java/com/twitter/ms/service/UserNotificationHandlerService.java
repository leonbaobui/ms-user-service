package com.twitter.ms.service;

import lombok.RequiredArgsConstructor;
import com.twitter.ms.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserNotificationHandlerService {

    private final UserRepository userRepository;

    @Transactional
    public void resetNotificationCount(Long userId) {
        userRepository.resetNotificationCount(userId);
    }
}
