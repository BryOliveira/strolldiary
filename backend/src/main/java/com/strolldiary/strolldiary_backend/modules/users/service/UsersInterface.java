package com.strolldiary.strolldiary_backend.modules.users.service;

import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersDto;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersLoginDto;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersRegistrationDto;

public interface UsersInterface {
  public UsersDto registerUser(UsersRegistrationDto registrationDto);
  public UsersDto authenticateUser(UsersLoginDto loginDto);
  public UsersDto getUserProfile(int id);
  public UsersDto updateUserProfile(UsersRegistrationDto toUpdateDto, String newUsername, String passwordAttempt);
  public UsersDto deleteUserAccount(UsersRegistrationDto registrationDto, String passwordAttempt);
}
