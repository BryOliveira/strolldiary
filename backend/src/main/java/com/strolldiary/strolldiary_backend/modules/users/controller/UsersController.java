package com.strolldiary.strolldiary_backend.modules.users.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersDto;
import com.strolldiary.strolldiary_backend.modules.users.model.dto.UsersLoginDto;
import com.strolldiary.strolldiary_backend.modules.users.service.UsersInterface;

/** 
 * Notes for tomorrow to make this work:
 * Replace return types with ResponseEntity<UsersDto>
 * Implement request body parsing for POST and PUT methods
 * Implement proper error handling and response codes
 * Add authentication and authorization checks using body not headers or params
 * Implement input validation and sanitization
 */

@RestController
@RequestMapping("/api/users")
@Validated
public class UsersController {

  private UsersInterface usersService;

  public UsersController(UsersInterface usersService) {
    this.usersService = usersService;
  }

  @GetMapping("/{id}")
  public UsersDto getUserInformation(@PathVariable int id) {
    if (id <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid user ID");
    }
    try {
      return usersService.getUserProfile(id);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e.getCause());
    }
  }
  
  @PostMapping("/api/users/register")
  public String createUserAccount(@RequestBody String entity) {
    //TODO: process POST request
    
    return entity;
  }
  
  @PostMapping("/api/users/login")
  public UsersDto loginUser(@RequestParam String username, 
                          @RequestParam String password) {
    if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid login data");
    }
    try {
      UsersLoginDto loginDto = new UsersLoginDto();
      loginDto.setUsername(username);
      loginDto.setPassword(password);
      return usersService.authenticateUser(loginDto);
    
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Incorrect username or password", e.getCause());
    }
    
  }

  @PutMapping("/api/users/{id}")
  public String updateUserInformation(@PathVariable String id, @RequestBody String entity) {
    //TODO: process PUT request
    
    return entity;
  }

  @DeleteMapping("/api/users/{id}")
  public void deleteUserAccount(@PathVariable String id) {

  }
}
