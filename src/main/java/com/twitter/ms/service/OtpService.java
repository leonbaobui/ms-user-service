package com.twitter.ms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OtpService {
    private static final int MAX_LIFE_TIME = 5;
    private static final int MAX_DIGITS = 7;

    private final RedisTemplate<String, String> redisTemplate;

    public String generateOtp(String key) {
        String otp = UUID.randomUUID().toString().substring(0, MAX_DIGITS);
        redisTemplate.opsForValue().set(key, otp, MAX_LIFE_TIME, TimeUnit.MINUTES);
        return otp;
    }

    public Optional<String> getOtp(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    public Boolean deleteOtp(String key) {
        return redisTemplate.delete(key);
    }
}

