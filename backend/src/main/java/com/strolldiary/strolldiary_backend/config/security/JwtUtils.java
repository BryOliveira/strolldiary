package com.strolldiary.strolldiary_backend.config.security;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.logging.Logger;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.strolldiary.strolldiary_backend.config.security.interfaces.TokenProvider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

// Will generate, parse, and validate JWTs
@Component
public class JwtUtils implements TokenProvider {

  private static final Logger LOGGER = Logger.getLogger(JwtUtils.class.getName());
  private final String jwtSecret;

  public JwtUtils(@Value("${jwt.secret}") String jwtSecret) {
    this.jwtSecret = jwtSecret;
  }

  @Override
  public String generateToken(int userId, String username, int expirationMillis) {
    return Jwts.builder()
               .subject(String.valueOf(userId))
               .claim("username", username)
               .issuedAt(new Date(System.currentTimeMillis()))
               .expiration(new Date(System.currentTimeMillis() + expirationMillis))
               .signWith(getKey())
               .compact();
  }

  @Override
  public String getUserIDFromToken(String token) {
    try {
      return Jwts.parser()
                 .verifyWith(getKey())
                 .build()
                 .parseSignedClaims(token)
                 .getPayload()
                 .getSubject();
    } catch (Exception e) {
      LOGGER.warning("Failed to extract user ID from JWT: " + e.getMessage());
      return null;
    }
  }
  
  @Override
  public ZonedDateTime getExpirationDateFromToken(String token) {
    try {
      return ZonedDateTime.ofInstant(Jwts.parser()
      .verifyWith(getKey())
      .build()
      .parseSignedClaims(token)
      .getPayload()
      .getExpiration().toInstant(), ZoneId.systemDefault());
    } catch (Exception e) {
      LOGGER.warning("Failed to extract expiration date from JWT: " + e.getMessage());
      return null;
    }
  }

  @Override
  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(getKey())
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (Exception e) {
      LOGGER.warning("JWT validation error: " + e.getMessage());
      return false;
    }
  }

  @Override
  public boolean isTokenExpired(String token) {
    try {
      Date expiration = Jwts.parser()
          .verifyWith(getKey())
          .build()
          .parseSignedClaims(token)
          .getPayload()
          .getExpiration();
      return expiration.before(new Date());
    } catch (Exception e) {
      LOGGER.warning("Failed to check if JWT is expired: " + e.getMessage());
      return true;
    }
  }

  public SecretKey getKey() {
    return Keys.hmacShaKeyFor(jwtSecret.getBytes());
  }
}
