package com.strolldiary.strolldiary_backend.modules.users.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UsersDto {
  @NotBlank
  private int id;
  @NotBlank
  private String username;
  @NotBlank
  @Email
  private String email;
  @NotBlank
  private String token;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public String getToken() {
    return token;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setToken(String token) {
    this.token = token;
  }
}

