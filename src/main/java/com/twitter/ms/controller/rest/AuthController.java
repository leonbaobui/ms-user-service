package com.twitter.ms.controller.rest;

import com.gmail.merikbest2015.dto.CommonResponse;
import com.twitter.ms.dto.request.AuthRequest;
import com.twitter.ms.dto.request.PasswordRegistrationRequest;
import com.twitter.ms.dto.request.RegistrationEmailCodeProcessRequest;
import com.twitter.ms.dto.request.RegistrationRequest;
import com.twitter.ms.dto.response.AuthResponse;
import com.twitter.ms.dto.response.RegistrationResponse;
import com.twitter.ms.service.AuthService;
import com.twitter.ms.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.gmail.merikbest2015.constants.PathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UI_V1 + "/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final AuthService authService;

    @PostMapping(path = LOGIN)
    public ResponseEntity<AuthResponse> loginController(
             @RequestHeader HttpHeaders headers,
             @RequestBody AuthRequest authRequest) {
        AuthResponse authResponse = authService.login(authRequest);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    @PostMapping(path=REGISTRATION_CHECK)
    public ResponseEntity<RegistrationResponse> registerValidateController(
            @RequestHeader HttpHeaders headers,
           @RequestBody RegistrationRequest userRegisterRequest) {
        RegistrationResponse response = registrationService.registrationValidateService(userRegisterRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(path=REGISTRATION_CODE)
    public ResponseEntity<CommonResponse> sendRegistrationCodeController(
            @RequestHeader HttpHeaders headers,
            @RequestBody RegistrationEmailCodeProcessRequest registrationEmailCodeProcessRequest) {
        CommonResponse commonResponse = registrationService.sendRegistrationCode(registrationEmailCodeProcessRequest.getEmail());
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @GetMapping(path = REGISTRATION_ACTIVATE_CODE)
    public ResponseEntity<CommonResponse> validatedActivationCodeController(
        @RequestHeader HttpHeaders headers,
        @PathVariable(name = "email") String email,
        @PathVariable(name = "code") String code) {
        CommonResponse commonResponse = registrationService.validatedActivationCode(email, code);
        return new ResponseEntity<>(commonResponse, HttpStatus.OK);
    }

    @PostMapping(path = REGISTRATION_CONFIRM)
    public ResponseEntity<AuthResponse> passwordConfirmationController(
            @RequestHeader HttpHeaders headers,
            @RequestBody PasswordRegistrationRequest request) {
        AuthResponse authResponse = registrationService.passwordConfirmation(request);
        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
