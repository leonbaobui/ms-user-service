package com.twitter.ms.controller.api;

import com.twitter.ms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import main.java.com.leon.baobui.dto.response.user.UserPrincipalResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static main.java.com.leon.baobui.constants.PathConstants.API_V1;
import static main.java.com.leon.baobui.constants.PathConstants.USER_EMAIL;

@RestController
@RequestMapping(value = API_V1 + "/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final UserService userService;

    @GetMapping(USER_EMAIL)
    public ResponseEntity<UserPrincipalResponse> getUserByEmail(
            @RequestHeader @Valid HttpHeaders headers,
            @PathVariable(name = "email") String email
    ) {
        UserPrincipalResponse userPrincipalResponse = userService.getUserByEmail(email);
        return new ResponseEntity<>(userPrincipalResponse, HttpStatus.OK);
    }
}
