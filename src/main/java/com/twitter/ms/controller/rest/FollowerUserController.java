package com.twitter.ms.controller.rest;

import com.gmail.merikbest2015.dto.CommonResponse;
import com.gmail.merikbest2015.dto.HeaderResponse;
import com.gmail.merikbest2015.dto.response.user.UserResponse;
import com.gmail.merikbest2015.util.AuthUtil;
import com.twitter.ms.dto.response.FollowerUserResponse;
import com.twitter.ms.repository.FollowerUserRepository;
import com.twitter.ms.service.FollowerUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gmail.merikbest2015.constants.PathConstants.*;

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
