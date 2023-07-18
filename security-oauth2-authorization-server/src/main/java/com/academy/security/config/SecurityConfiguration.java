package com.academy.security.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import com.academy.security.domain.SecurityUser;
import com.academy.security.domain.User;
import com.academy.security.repository.UserRepository;

@Configuration
@EnableWebSecurity(debug = true)
public class SecurityConfiguration {

  @Autowired
  private UserRepository userRepository;

  @Bean
  @Order(1)
  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
    http
        .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
        .oidc(Customizer.withDefaults());
    return http.formLogin(Customizer.withDefaults()).build();
  }

  @Bean
  @Order(2)
  public SecurityFilterChain standardSecurityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers("/h2-console/**").permitAll()
            .anyRequest().authenticated())
        .csrf((csrf) -> csrf.disable())
        .httpBasic(Customizer.withDefaults())
        .oauth2ResourceServer((oauth2) -> oauth2
            .jwt(Customizer.withDefaults()))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling((exceptions) -> exceptions
            .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
            .accessDeniedHandler(new BearerTokenAccessDeniedHandler()));
    return http.build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      // 根据用户名查询用户信息，这里假设使用userRepository来获取用户信息
      return userRepository
          .findByUsername(username)
          .map(SecurityUser::new)
          .orElseThrow(() -> new UsernameNotFoundException("Username not found: " + username));
    };
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    String noopIdentifier = "noop";
    PasswordEncoder defaultEncoder = new BCryptPasswordEncoder();

    DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(noopIdentifier,
        Map.of(noopIdentifier, NoOpPasswordEncoder.getInstance(),
            "bcrypt", defaultEncoder));

    delegatingPasswordEncoder.setDefaultPasswordEncoderForMatches(defaultEncoder);

    return delegatingPasswordEncoder;
  }

  @Bean
  public CommandLineRunner init(UserRepository userRepository, PasswordEncoder encoder) {
    return args -> {
      System.out.println("Initialization code here..." + encoder.encode("secret"));

      // 初始化用户数据
      User user1 = new User("user1@abc.com", encoder.encode("password"), "ROLE_USER");
      userRepository.save(user1);

      User user2 = new User("user2@abc.com", encoder.encode("password"), "ROLE_USER,ROLE_ADMIN");
      userRepository.save(user2);
    };
  }

}
