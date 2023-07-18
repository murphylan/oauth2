package com.academy.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.security.domain.User;
import com.academy.security.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @PostMapping
  public User createUser(@RequestBody User request) {
    return userService.createUser(request.getUsername(), request.getPassword(), request.getRoles());
  }

  @GetMapping
  public List<User> getAllUsers() {
    return userService.getAllUsers();
  }

}