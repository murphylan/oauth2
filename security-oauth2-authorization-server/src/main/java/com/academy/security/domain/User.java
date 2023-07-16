package com.academy.security.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

  @Id
  @GeneratedValue
  private Long id;
  private String username;
  private String password;
  private String roles;

  public User() {
  }

  public User(String username, String password, String roles) {
    this.username = username;
    this.password = password;
    this.roles = roles;
  }
}