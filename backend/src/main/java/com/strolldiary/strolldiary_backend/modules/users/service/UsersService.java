package com.strolldiary.strolldiary_backend.modules.users.service;

import org.springframework.stereotype.Service;

import com.strolldiary.strolldiary_backend.common.sec.EncoderProvider;
import com.strolldiary.strolldiary_backend.common.sec.TokenProvider;
import com.strolldiary.strolldiary_backend.modules.users.model.Users;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersDto;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersLoginDto;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersRegistrationDto;
import com.strolldiary.strolldiary_backend.modules.users.repo.UsersRepo;

@Service
public class UsersService implements UsersInterface {
  private static final int TOKEN_EXPIRATION_TIME = 15 * 60 * 1000; // 15 minutes
  
  private final UsersRepo usersRepo;
  private final TokenProvider tokenProvider;
  private final EncoderProvider encoderProvider;

  public UsersService(UsersRepo usersRepo, TokenProvider tokenProvider, EncoderProvider encoderProvider) {
    this.usersRepo = usersRepo;
    this.tokenProvider = tokenProvider;
    this.encoderProvider = encoderProvider;
  }

  @Override
  public UsersDto registerUser(UsersRegistrationDto registrationDto) {
    if (usersRepo.findByUsername(registrationDto.getUsername()) != null) {
      throw new IllegalArgumentException("Username already exists");
    }
    if (usersRepo.findByEmail(registrationDto.getEmail()) != null) {
      throw new IllegalArgumentException("Email already exists");
    }

    Users newUser = new Users();
    newUser.setUsername(registrationDto.getUsername());
    newUser.setEmail(registrationDto.getEmail());
    newUser.setPassword(encoderProvider.passwordEncoder().encode(registrationDto.getPassword()));

    registrationDto.setPassword(null);

    Users savedUser = usersRepo.save(newUser);
    UsersDto userDto = convertToDto(savedUser);
    String token = tokenProvider.generateToken(savedUser.getId(), savedUser.getUsername(), TOKEN_EXPIRATION_TIME);
    userDto.setToken(token);
    return userDto;
  }

  @Override
  public UsersDto authenticateUser (UsersLoginDto loginDto) {
    Users user = usersRepo.findByUsername(loginDto.getUsername());
    if (user == null || !encoderProvider.passwordEncoder().matches(loginDto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Invalid username or password");
    }


    UsersDto userDto = convertToDto(user);
    loginDto.setPassword(null);

    String token = tokenProvider.generateToken(user.getId(), user.getUsername(), TOKEN_EXPIRATION_TIME);
    userDto.setToken(token);
    return userDto;
  }

  @Override
  public UsersDto getUserProfile(int id) {
    Users user = usersRepo.findById(id).orElse(null);
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }

    return convertToDto(user);
  }

  @Override
  public UsersDto updateUserProfile(UsersRegistrationDto toUpdateDto, String newUsername, String passwordAttempt) {
    Users user = usersRepo.findById(toUpdateDto.getId()).orElse(null);
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    if (!encoderProvider.passwordEncoder().matches(passwordAttempt, user.getPassword())) {
      throw new IllegalArgumentException("Incorrect password");
    }
    if (!toUpdateDto.getUsername().equals(newUsername) && usersRepo.findByUsername(newUsername) != null) {
      throw new IllegalArgumentException("New username already exists");
    }
    if (!toUpdateDto.getEmail().equals(user.getEmail()) && usersRepo.findByEmail(toUpdateDto.getEmail()) != null) {
      throw new IllegalArgumentException("Email already exists");
    }

    user.setUsername(newUsername);
    user.setEmail(toUpdateDto.getEmail());

    // Only update password if a new one is provided and it's different
    if (!toUpdateDto.getPassword().isEmpty() && 
        !encoderProvider.passwordEncoder().matches(toUpdateDto.getPassword(), user.getPassword())) {
        user.setPassword(encoderProvider.passwordEncoder().encode(toUpdateDto.getPassword()));
    }

    Users updatedUser = usersRepo.save(user);
    return convertToDto(updatedUser);
  }

  @Override
  public UsersDto deleteUserAccount(UsersRegistrationDto registrationDto, String passwordAttempt) {
    Users user = usersRepo.findByUsername(registrationDto.getUsername());
    if (user == null) {
      throw new IllegalArgumentException("User not found");
    }
    if (!encoderProvider.passwordEncoder().matches(passwordAttempt, user.getPassword())) {
      throw new IllegalArgumentException("Incorrect password");
    }
    
    usersRepo.delete(user);
    return convertToDto(user);
  }

  private UsersDto convertToDto(Users user) {
    UsersDto userDto = new UsersDto();
    userDto.setId(user.getId());
    userDto.setUsername(user.getUsername());
    userDto.setEmail(user.getEmail());
    return userDto;
  }
}
