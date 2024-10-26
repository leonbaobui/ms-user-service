package com.twitter.ms.security.config;

import lombok.RequiredArgsConstructor;

import com.twitter.ms.security.oauth2.GoogleOAuth2UserService;
import com.twitter.ms.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.twitter.ms.security.oauth2.OAuth2LoginSuccessHandler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final GoogleOAuth2UserService googleOAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    /*
      By default, Spring OAuth2 uses HttpSessionOAuth2AuthorizationRequestRepository to save
      the authorization request. But, since our service is stateless, we can't save it in
      the session. We'll save the request in a Base64 encoded cookie instead.
    */
    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurityFilterChain) throws Exception {
        // we have already the security filter chain handled by gateway
        // so basically here allow all authorize requests for other security filter (future)
        httpSecurityFilterChain
                .sessionManagement(httpSecuritySessionManagementConfigurer ->
                        httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(httpSecurityCsrfConfigurer ->
                        httpSecurityCsrfConfigurer.disable()
                )
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer.disable()
                )
                .oauth2Login(oauth2Login ->
                    oauth2Login
                            .authorizationEndpoint(authorizationEndpointConfig -> {
                                authorizationEndpointConfig
                                        .baseUri("/ui/v1/auth/oauth2/authorize")
                                        .authorizationRequestRepository(cookieAuthorizationRequestRepository());
                            })
                            .redirectionEndpoint(redirectionEndpointConfig -> {
                                redirectionEndpointConfig.baseUri("/ui/v1/auth/oauth2/callback/*");
                            })
                            .userInfoEndpoint(customizer -> customizer.userService(googleOAuth2UserService))
                            .successHandler(oAuth2LoginSuccessHandler)
                );
        return httpSecurityFilterChain.build();
    }

}
