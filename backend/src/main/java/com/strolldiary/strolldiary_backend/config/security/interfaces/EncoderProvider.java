package com.strolldiary.strolldiary_backend.config.security.interfaces;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface EncoderProvider {
  PasswordEncoder passwordEncoder();
}
