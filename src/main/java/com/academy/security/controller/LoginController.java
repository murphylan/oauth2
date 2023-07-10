package com.academy.security.controller;

import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

  @GetMapping("/")
  public Map<String, Object> index(@AuthenticationPrincipal OAuth2User principal) {
    return principal.getAttributes();
  }

  @GetMapping("/token")
  public OAuth2AccessToken token(@RegisteredOAuth2AuthorizedClient("github") OAuth2AuthorizedClient authorizedClient) {
    OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
    return accessToken;
  }
}
