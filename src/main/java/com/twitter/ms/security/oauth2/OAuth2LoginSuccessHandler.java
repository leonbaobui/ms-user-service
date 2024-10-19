package com.twitter.ms.security.oauth2;

import java.io.IOException;

import lombok.RequiredArgsConstructor;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import main.java.com.leon.baobui.security.JwtProvider;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final GoogleOAuth2UserService googleOAuth2UserService;
    private final JwtProvider jwtProvider;
    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        GoogleOAuth2User googleOAuth2User = (GoogleOAuth2User) token.getPrincipal();
        String email = googleOAuth2User.getEmail();
        String idAttributeKey;

        if ("google".equals(token.getAuthorizedClientRegistrationId())) {
            idAttributeKey = "sub";
            googleOAuth2UserService.oauth2Register(token, idAttributeKey);
        }

        this.setAlwaysUseDefaultTargetUrl(true);

        String accessToken = jwtProvider.createToken(email, "USER");

        String targetUrl = UriComponentsBuilder.fromUriString(frontendUrl + "/oauth2/redirect")
                .queryParam("token", accessToken)
                .build().toUriString();

        this.setDefaultTargetUrl(targetUrl);
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
