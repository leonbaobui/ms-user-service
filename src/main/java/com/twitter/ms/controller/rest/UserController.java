package com.twitter.ms.controller.rest;

import com.gmail.merikbest2015.dto.CommonResponse;
import com.gmail.merikbest2015.dto.HeaderResponse;
import com.gmail.merikbest2015.dto.response.user.UserResponse;
import com.gmail.merikbest2015.util.AuthUtil;
import com.twitter.ms.dto.request.UserRequest;
import com.twitter.ms.dto.response.AuthResponse;
import com.twitter.ms.dto.response.AuthUserResponse;
import com.twitter.ms.dto.response.UserProfileResponse;
import com.twitter.ms.model.User;
import com.twitter.ms.service.AuthService;
import com.twitter.ms.service.UserService;
import com.twitter.ms.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.gmail.merikbest2015.constants.PathConstants.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UI_V1 + USER)
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @GetMapping(START)
    public ResponseEntity<Boolean> startUseTwitter() {
        userService.startUseTwitter();
        return ResponseEntity.ok(true);
    }

    @GetMapping(TOKEN)
    public ResponseEntity<AuthResponse> getUserByToken() {
        String authUserId = String.valueOf(AuthUtil.getAuthenticatedUserId());
        return ResponseEntity.ok(authService.getUserByToken(authUserId));
    }
    @GetMapping(USER_ID)
    public ResponseEntity<UserProfileResponse> getUserByUserId (
            @RequestHeader @Valid HttpHeaders headers,
            @PathVariable(name = "userId") Long userId
        ) {
        UserProfileResponse userProfileResponse = userService.getUserProfileById(userId);
        return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
    }

    // Follow user flow
    @GetMapping(ALL)
    public ResponseEntity<List<UserResponse>> getUsers(@PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<UserResponse> response = userService.getUsers(pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @PostMapping(UPLOAD)
    public ResponseEntity<String> uploadImageUserProfile(@RequestPart("file") MultipartFile multipartFile) {
        return ResponseEntity.ok(userService.uploadImageUserProfile(multipartFile));
    }
    
    @PutMapping
    public ResponseEntity<AuthUserResponse> updateUserProfile(@Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUserProfile(userRequest));
    }

    @GetMapping(SEARCH_USERNAME)
    public ResponseEntity<List<UserResponse>> searchUsersByUsername(
            @PathVariable("username") String username,
            @PageableDefault(size = 15) Pageable pageable) {
        List<UserResponse> userResponses = userService.searchUsersByUsername(username, pageable);
        return ResponseEntity.ok(userResponses);
    }

//    @GetMapping("/token")
//    public ResponseEntity<AuthResponse> getUserByToken(
//            HttpServletRequest request
//    ) {
//        String userId = Utils.parseUserId(request);
//        AuthResponse authResponse = authService.getUserByToken(userId);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

}
