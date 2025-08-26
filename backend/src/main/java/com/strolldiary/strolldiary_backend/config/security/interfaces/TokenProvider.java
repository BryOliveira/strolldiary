package com.strolldiary.strolldiary_backend.config.security.interfaces;

import java.time.ZonedDateTime;

public interface TokenProvider {
  String generateToken(int userId, String username, int expirationMillis);
  String getUserIDFromToken(String token);
  ZonedDateTime getExpirationDateFromToken(String token);
  boolean validateToken(String token);
  boolean isTokenExpired(String token);
}
