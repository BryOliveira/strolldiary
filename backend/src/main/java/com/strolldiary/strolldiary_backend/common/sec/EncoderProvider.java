package com.strolldiary.strolldiary_backend.common.sec;

import org.springframework.security.crypto.password.PasswordEncoder;

public interface EncoderProvider {
  PasswordEncoder passwordEncoder();
}
