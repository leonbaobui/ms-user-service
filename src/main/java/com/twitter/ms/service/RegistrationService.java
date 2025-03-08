package com.twitter.ms.service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.lib.twitter.lib_trans_outbox.domain.OutboxEvent;
import com.lib.twitter.lib_trans_outbox.service.TransactionOutboxService;
import com.twitter.ms.dto.request.PasswordRegistrationRequest;
import com.twitter.ms.dto.request.RegistrationRequest;
import com.twitter.ms.dto.response.AuthResponse;
import com.twitter.ms.dto.response.RegistrationResponse;
import com.twitter.ms.exception.RegistrationException;
import com.twitter.ms.mapper.UserMapper;
import com.twitter.ms.model.User;
import com.twitter.ms.model.UserKey;
import com.twitter.ms.model.UserValue;
import com.twitter.ms.producer.AmqpProducer;
import com.twitter.ms.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import main.java.com.leon.baobui.dto.CommonResponse;
import main.java.com.leon.baobui.dto.request.EmailRequest;
import main.java.com.leon.baobui.security.JwtProvider;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {
    private static final String TOPIC = "user-service.user.user-registered-event";
    private final UserRepository userRepository;
    private final AmqpProducer amqpProducer;
    private final TransactionOutboxService transactionOutboxService;
    private final OtpService otpService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public RegistrationResponse registrationValidateService(RegistrationRequest request) {
        String email = request.getEmail();
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isEmpty()) {
            User user = UserMapper.INSTANCE.registrationRequestToUserDAO(request);
            userRepository.saveAndFlush(user);
            return RegistrationResponse.builder()
                    .username(email)
                    .message("Registered successfully")
                    .build();
        }

        if (!existingUser.get().isActive()) {
            existingUser.get().setUsername(request.getUsername());
            existingUser.get().setFullName(request.getUsername());
            existingUser.get().setBirthday(request.getBirthday());
            userRepository.saveAndFlush(existingUser.get());
            return RegistrationResponse.builder()
                    .username(email)
                    .message("Re-activate account")
                    .build();
        }
        throw new RegistrationException("Email", "Email is already registered", HttpStatus.FORBIDDEN);
    }

    @Transactional
    public CommonResponse sendRegistrationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(
                        () -> new RegistrationException("Email",
                                "Email is already registered",
                                HttpStatus.FORBIDDEN));
        userRepository.updateActivationCode(otpService.generateOtp(user.getEmail()), user.getId());
        String activationCode = userRepository.findActivationCodeByUserId(user.getId());
        log.info("Ready to send validation code to email={}", user.getEmail());
        EmailRequest emailRequest = EmailRequest.builder()
                .to(user.getEmail())
                .subject("Validation code for Twitter registration")
                .template("Template")
                .attributes(Map.of(
                        "fullName", user.getFullName(),
                        "registrationCode", activationCode
                ))
                .build();
        amqpProducer.sendEmail(emailRequest);
        log.info("Email has been sent successfully to rabbit mq");
        return CommonResponse.builder()
                .httpStatus("200 OK")
                .message("Send activation code successfully")
                .build();
    }

    @Transactional
    public CommonResponse validatedActivationCode(String email, String activationCode) {
        Optional<String> otpOptional = otpService.getOtp(email);
        String otp = otpOptional.orElseThrow(
                () -> new RegistrationException("Activation code",
                        "Activation code not found/or passed TTL",
                        HttpStatus.FORBIDDEN));
        if (!otp.equals(activationCode)) {
            throw new RegistrationException("Activation code", "Activation code invalid", HttpStatus.FORBIDDEN);
        }
        otpService.deleteOtp(email);
        return CommonResponse.builder()
                .httpStatus("200 OK")
                .message("Activated account successfully")
                .build();
    }

    @Transactional
    public AuthResponse passwordConfirmation(PasswordRegistrationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RegistrationException("Email", "Email/user not found", HttpStatus.FORBIDDEN));
        userRepository.updatePassword(passwordEncoder.encode(request.getPassword()), user.getId());
        userRepository.updateAccountStatus(true, user.getId());
        var userValue = UserMapper.INSTANCE.userEntityToUserEventDTO(user);
        var outboxEvent = OutboxEvent.<UserKey, UserValue>builder()
                .eventType("user created")
                .rootEntityType("User")
                .rootEntityId(String.valueOf(userValue.getId()))
                .idempotencyKey(UUID.randomUUID().toString())
                .topic(TOPIC)
                .key(UserKey.newBuilder()
                        .setId(userValue.getId())
                        .build())
                .payload(userValue)
                .build();
        transactionOutboxService.saveEventToOutboxTable(outboxEvent);
        String accessToken = jwtProvider.createToken(user.getEmail(), "USER");
        return AuthResponse.builder()
                .user(UserMapper.INSTANCE.userEntityToAuthUserDTO(user))
                .token(accessToken)
                .build();
    }


}
