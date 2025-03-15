package com.twitter.ms.controller.rest;

import java.util.List;

import lombok.RequiredArgsConstructor;
import com.twitter.ms.dto.request.UserRequest;
import com.twitter.ms.dto.response.AuthResponse;
import com.twitter.ms.dto.response.AuthUserResponse;
import com.twitter.ms.dto.response.UserDetailResponse;
import com.twitter.ms.dto.response.UserProfileResponse;
import com.twitter.ms.service.AuthService;
import com.twitter.ms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import main.java.com.leon.baobui.dto.HeaderResponse;
import main.java.com.leon.baobui.dto.response.user.UserResponse;
import main.java.com.leon.baobui.util.AuthUtil;

import static main.java.com.leon.baobui.constants.PathConstants.ALL;
import static main.java.com.leon.baobui.constants.PathConstants.DETAILS_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.RELEVANT;
import static main.java.com.leon.baobui.constants.PathConstants.SEARCH_USERNAME;
import static main.java.com.leon.baobui.constants.PathConstants.START;
import static main.java.com.leon.baobui.constants.PathConstants.TOKEN;
import static main.java.com.leon.baobui.constants.PathConstants.UI_V1;
import static main.java.com.leon.baobui.constants.PathConstants.UPLOAD;
import static main.java.com.leon.baobui.constants.PathConstants.USER;
import static main.java.com.leon.baobui.constants.PathConstants.USER_ID;

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
    public ResponseEntity<UserProfileResponse> getUserByUserId(
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

    @GetMapping(DETAILS_USER_ID)
    public ResponseEntity<UserDetailResponse> getUserDetails(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

    @GetMapping(RELEVANT)
    public ResponseEntity<List<UserResponse>> getRelevantUsers() {
        return ResponseEntity.ok(userService.getRelevantUsers());
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
