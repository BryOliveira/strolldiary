package com.strolldiary.strolldiary_backend.modules.users.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.strolldiary.strolldiary_backend.common.sec.EncoderProvider;
import com.strolldiary.strolldiary_backend.common.sec.TokenProvider;
import com.strolldiary.strolldiary_backend.modules.users.model.Users;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersDto;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersLoginDto;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersRegistrationDto;
import com.strolldiary.strolldiary_backend.modules.users.repo.UsersRepo;

class UsersServiceTest {
  @Mock
  private UsersRepo userRepo;

  @Mock
  private TokenProvider tokenProvider;

  @Mock
  private EncoderProvider encoderProvider;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UsersService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(encoderProvider.passwordEncoder()).thenReturn(passwordEncoder);
    userService = new UsersService(userRepo, tokenProvider, encoderProvider);
  }

  @Test
  void testRegisterUser() {
    UsersRegistrationDto regDto = new UsersRegistrationDto();
    regDto.setUsername("newuser");
    regDto.setEmail("test@email.com");
    regDto.setPassword("newpassword");

    Users savedUser = new Users();
    savedUser.setId(1);
    savedUser.setUsername("newuser");
    savedUser.setEmail("test@email.com");
    savedUser.setPassword("hashedpassword");

    when(userRepo.findByUsername("newuser")).thenReturn(null);
    when(userRepo.findByEmail("test@email.com")).thenReturn(null);
    when(passwordEncoder.encode("newpassword")).thenReturn("hashedpassword");
    when(userRepo.save(any(Users.class))).thenReturn(savedUser);

    UsersDto dto = userService.registerUser(regDto);
    assertNotNull(dto);
    assertEquals("newuser", dto.getUsername());
    assertEquals("test@email.com", dto.getEmail());
  }

  @Test
  void testRegisterUserDuplicateUser() {
    UsersRegistrationDto regDto = new UsersRegistrationDto();
    regDto.setUsername("testuser");
    regDto.setEmail("new@email.com");
    regDto.setPassword("newpassword");

    Users existingUser = new Users();
    existingUser.setId(1);
    existingUser.setUsername("testuser");
    existingUser.setEmail("new@email.com");
    existingUser.setPassword("newpassword");

    when(userRepo.findByUsername("testuser")).thenReturn(existingUser);

    assertThrows(Exception.class, () -> userService.registerUser(regDto));
  }

  @Test
  void testRegisterInvalid() {
    assertThrows(Exception.class, () -> userService.registerUser(null));
  }

  @Test
  void testAuthentication() {
    UsersLoginDto loginDto = new UsersLoginDto();
    loginDto.setUsername("testuser");
    loginDto.setPassword("password");

    Users user = new Users();
    user.setId(1);
    user.setUsername("testuser");
    user.setPassword("password");
    when(userRepo.findByUsername("testuser")).thenReturn(user);
    when(passwordEncoder.matches("password", "password")).thenReturn(true);

    UsersDto result = userService.authenticateUser(loginDto);
    assertNotNull(result);
    assertEquals("testuser", result.getUsername());
    assertEquals(1, result.getId());
  }

  @Test
  void testAuthenticationWrongPassword() {
    UsersLoginDto loginDto = new UsersLoginDto();
    loginDto.setUsername("testuser");
    loginDto.setPassword("wrongpassword");

    Users user = new Users();
    user.setId(1);
    user.setUsername("testuser");
    user.setPassword("password");

    when(userRepo.findByUsername("testuser")).thenReturn(user);

    assertThrows(Exception.class, () -> userService.authenticateUser(loginDto));
  }

  @Test
  void testGetUserProfile() {
    Users user = new Users();
    user.setId(2);
    user.setUsername("profileuser");
    user.setEmail("em@ail.mail");
    user.setPassword("profilepassword");
    
    when(userRepo.findById(2)).thenReturn(Optional.of(user));
    
    UsersDto dto = userService.getUserProfile(2);
    assertNotNull(dto);
    assertEquals("profileuser", dto.getUsername());
    assertEquals("em@ail.mail", dto.getEmail());
  }
  
  @Test
  void testGetUserProfileNotFound() {
    when(userRepo.findByUsername("nouser")).thenReturn(null);
    assertThrows(IllegalArgumentException.class, () -> userService.getUserProfile(999));
  }

  @Test
  void testUpdateUserProfile() {
    UsersRegistrationDto regDto = new UsersRegistrationDto();
    regDto.setId(3);
    regDto.setUsername("usertoupdate");
    regDto.setEmail("testing@email.com");
    regDto.setPassword("pword");

    Users user = new Users();
    user.setId(3);
    user.setUsername("usertoupdate");
    user.setEmail("testing@email.com");
    user.setPassword("pword");

    when(userRepo.findById(3)).thenReturn(Optional.of(user));
    when(userRepo.save(any(Users.class))).thenAnswer(i -> i.getArgument(0));
    when(passwordEncoder.matches("pword", "pword")).thenReturn(true);

    userService.updateUserProfile(regDto, "updateduser", "pword");

    Users updatedUser = new Users();
    updatedUser.setId(3);
    updatedUser.setUsername("updateduser");
    updatedUser.setEmail("testing@email.com");
    updatedUser.setPassword("pword");
    when(userRepo.findByUsername("updateduser")).thenReturn(updatedUser);

    UsersDto dto = userService.getUserProfile(3);
    assertNotNull(dto);
    assertEquals("updateduser", dto.getUsername());
  }

  @Test
  void testDeleteUser() {
    UsersRegistrationDto regDto = new UsersRegistrationDto();
    regDto.setUsername("usertodelete");
    regDto.setEmail("del@et.com");
    regDto.setPassword("deletepassword");

    Users user = new Users();
    user.setId(4);
    user.setUsername("usertodelete");
    user.setEmail("del@et.com");
    user.setPassword("deletepassword");

    when(userRepo.findByUsername("usertodelete")).thenReturn(user);
    when(passwordEncoder.matches("deletepassword", "deletepassword")).thenReturn(true);

    doNothing().when(userRepo).delete(user);

    userService.deleteUserAccount(regDto, "deletepassword");

    when(userRepo.findByUsername("usertodelete")).thenReturn(null);
    assertThrows(IllegalArgumentException.class, () -> userService.getUserProfile(4));
  }

  @Test
  void testDeleteUserWrongPassword() {
    UsersRegistrationDto regDto = new UsersRegistrationDto();
    regDto.setUsername("wrongpassworduser");
    regDto.setEmail("del@et.com");
    regDto.setPassword("deletepassword");

    Users user = new Users();
    user.setId(5);
    user.setUsername("wrongpassworduser");
    user.setEmail("del@et.com");
    user.setPassword("deletepassword");

    when(userRepo.findByUsername("wrongpassworduser")).thenReturn(user);

    assertThrows(IllegalArgumentException.class, () -> userService.deleteUserAccount(regDto, "wrongpassword"));
  }
}
