package com.twitter.ms.security.config;

import lombok.RequiredArgsConstructor;

import com.twitter.ms.security.oauth2.GoogleOAuth2UserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final GoogleOAuth2UserService googleOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurityFilterChain) throws Exception {
        // we have already the security filter chain handled by gateway
        // so basically here allow all authorize requests for other security filter (future)
        httpSecurityFilterChain.authorizeHttpRequests(authorizeRequests ->
                authorizeRequests
                        .anyRequest().authenticated()
        ).oauth2Login(oauth2Login ->
            oauth2Login.userInfoEndpoint(customizer -> customizer.userService(googleOAuth2UserService))
        );
        return httpSecurityFilterChain.build();
    }

}
