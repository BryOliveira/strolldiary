package com.strolldiary.strolldiary_backend.config.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.strolldiary.strolldiary_backend.common.sec.EncoderProvider;

@Component
public class EncoderProviderImpl implements EncoderProvider {
  private final PasswordEncoder passwordEncoder;

  public EncoderProviderImpl(PasswordEncoder passwordEncoder) {
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public PasswordEncoder passwordEncoder() {
    return this.passwordEncoder;
  }
  
}
