package com.twitter.ms.service;

import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.mapper.BasicMapper;
import com.gmail.merikbest2015.security.JwtProvider;
import com.twitter.ms.dto.request.AuthRequest;
import com.twitter.ms.dto.response.AuthResponse;
import com.twitter.ms.dto.response.AuthUserResponse;
import com.twitter.ms.exception.DataNotFoundException;
import com.twitter.ms.mapper.UserMapper;
import com.twitter.ms.model.User;
import com.twitter.ms.repository.UserRepository;
import com.twitter.ms.repository.projection.AuthUserProjection;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.gmail.merikbest2015.constants.PathConstants.AUTH_USER_ID_HEADER;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final BasicMapper basicMapper;

    public AuthResponse login(AuthRequest authRequest) {
        User user = userRepository.findByEmail(authRequest.getEmail())
                        .orElseThrow(() -> new DataNotFoundException("User", "User/email is not existed", HttpStatus.NOT_FOUND));
        Boolean isMatched = passwordEncoder.matches(authRequest.getPassword(), user.getPassword());
        if (!isMatched) {
            throw new ApiRequestException("Incorrect password!", HttpStatus.FORBIDDEN);
        }
        String accessToken = jwtProvider.createToken(user.getEmail(), "USER");
        return AuthResponse.builder()
                .user(UserMapper.INSTANCE.userEntityToAuthUserDTO(user))
                .token(accessToken)
                .build();
    }

    public AuthResponse getUserByToken(String userId) {
        AuthUserProjection user = userRepository.getUserById(Long.parseLong(userId), AuthUserProjection.class)
                .orElseThrow(() -> new DataNotFoundException("User", "User/email is not existed", HttpStatus.NOT_FOUND));
        String accessToken = jwtProvider.createToken(user.getEmail(), "USER");
        return AuthResponse.builder()
                .user(basicMapper.convertToResponse(user, AuthUserResponse.class))
                .token(accessToken)
                .build();
    }

}
