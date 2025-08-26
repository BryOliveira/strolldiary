package com.strolldiary.strolldiary_backend.config.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MockAuthController {

  @PostMapping("/login")
  public ResponseEntity<?> login() {
    return ResponseEntity.ok().build();
  }

  @PostMapping("/register")
  public ResponseEntity<?> register() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/protected")
  public ResponseEntity<?> protectedEndpoint() {
    return ResponseEntity.ok().build();
  }

}
