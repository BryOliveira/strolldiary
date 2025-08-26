package com.strolldiary.strolldiary_backend.config.security;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JwtAuthenticationTest {
  private static final int TEST_EXPIRATION = 60 * 1000;
  private JwtUtils jwtUtils;
  private static final String JWT_TEST_SECRET = "testsecret_long_enough_for_testing";

  @BeforeEach
  void setUp() {
    jwtUtils = new JwtUtils(JWT_TEST_SECRET);
  }

  @Test
  void testValidToken() {
    String token = jwtUtils.generateToken(1, "testuser", TEST_EXPIRATION);
    assertTrue(jwtUtils.validateToken(token));
  }

  @Test
  void testInvalidToken() {
    String token = "invalid token";
    assertFalse(jwtUtils.validateToken(token));
  }

  @Test
  void testExpiredToken() {
    String token = jwtUtils.generateToken(1, "testuser", 0);
    assertFalse(jwtUtils.validateToken(token));
  }

  @Test
  void testGetUserIDFromToken() {
    String token = jwtUtils.generateToken(1, "testuser", TEST_EXPIRATION);
    assertEquals(1, Integer.parseInt(jwtUtils.getUserIDFromToken(token)));
  }

  @Test
  void testGetExpirationDateFromToken() {
    String token = jwtUtils.generateToken(1, "testuser", TEST_EXPIRATION);
    ZonedDateTime expirationDate = jwtUtils.getExpirationDateFromToken(token);
    ZonedDateTime expected = ZonedDateTime.now().plusSeconds(TEST_EXPIRATION / 1000);

    long secondsDifference = Math.abs(expirationDate.toEpochSecond() - expected.toEpochSecond());
    assertTrue(secondsDifference < 5);
  }


  @Test
  void testIsTokenExpired() {
    String token = jwtUtils.generateToken(1, "testuser", TEST_EXPIRATION);
    assertFalse(jwtUtils.isTokenExpired(token));
    token = jwtUtils.generateToken(1, "testuser", 0);
    assertTrue(jwtUtils.isTokenExpired(token));
  }


  @Test
  void testMalformedToken() {
    assertFalse(jwtUtils.validateToken(null));
    assertNull(jwtUtils.getUserIDFromToken(null));
    assertNull(jwtUtils.getExpirationDateFromToken(null));
    assertTrue(jwtUtils.isTokenExpired(null));
  }
}
