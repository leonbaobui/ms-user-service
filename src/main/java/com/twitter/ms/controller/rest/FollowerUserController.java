package com.twitter.ms.controller.rest;

import java.util.List;

import lombok.RequiredArgsConstructor;
import com.twitter.ms.service.FollowerUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import main.java.com.leon.baobui.dto.HeaderResponse;
import main.java.com.leon.baobui.dto.response.user.UserResponse;

import static main.java.com.leon.baobui.constants.PathConstants.FOLLOWERS_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.FOLLOWING_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.FOLLOW_USER_ID;
import static main.java.com.leon.baobui.constants.PathConstants.UI_V1;
import static main.java.com.leon.baobui.constants.PathConstants.USER;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = UI_V1 + USER)
public class FollowerUserController {
    private final FollowerUserService followerUserService;

    @GetMapping(FOLLOW_USER_ID)
    public ResponseEntity<Boolean> processFollow(@PathVariable(name = "userId") Long userId) {
        Boolean isFollowing = followerUserService.processFollow(userId);
        return ResponseEntity.ok(isFollowing);
    }

    @GetMapping(FOLLOWING_USER_ID)
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable("userId") Long userId,
                                                           @PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<UserResponse> response = followerUserService.getFollowing(userId, pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

    @GetMapping(FOLLOWERS_USER_ID)
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable("userId") Long userId,
                                                           @PageableDefault(size = 15) Pageable pageable) {
        HeaderResponse<UserResponse> response = followerUserService.getFollowers(userId, pageable);
        return ResponseEntity.ok().headers(response.getHeaders()).body(response.getItems());
    }

}
