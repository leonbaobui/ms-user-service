package com.twitter.ms.controller.api;

import com.gmail.merikbest2015.dto.response.user.UserPrincipalResponse;
import com.twitter.ms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.gmail.merikbest2015.constants.PathConstants.API_V1;
import static com.gmail.merikbest2015.constants.PathConstants.USER_EMAIL;

@RestController
@RequestMapping(value = API_V1 + "/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final UserService userService;
    @GetMapping(USER_EMAIL)
    public ResponseEntity<UserPrincipalResponse> getUserByEmail (
            @RequestHeader @Valid HttpHeaders headers,
            @PathVariable(name = "email") String email
    ) {
        UserPrincipalResponse userPrincipalResponse = userService.getUserByEmail(email);
        return new ResponseEntity<>(userPrincipalResponse, HttpStatus.OK);
    }
}
