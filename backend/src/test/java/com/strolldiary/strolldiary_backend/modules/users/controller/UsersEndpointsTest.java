package com.strolldiary.strolldiary_backend.modules.users.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import com.strolldiary.strolldiary_backend.common.sec.TokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:test-application.properties")
class UsersEndpointsTest {
  private static final int TEST_EXPIRATION = 60 * 1000;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TokenProvider tokenProvider;

  @Test
  void testGetUserId() throws Exception {
    mockMvc.perform(get("/api/users/1"))
           .andDo(print())
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  void testGetUsername() throws Exception {
    mockMvc.perform(get("/api/users/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.username").value("tester"));
  }

  @Test
  void testGetEmail() throws Exception {
    mockMvc.perform(get("/api/users/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.email").value("tester@example.com"));
  }

  @Test
  void testGetNonExistentUser() throws Exception {
    mockMvc.perform(get("/api/users/999"))
           .andExpect(status().isNotFound());
  }

  @Test
  void testGetUserInvalidId() throws Exception {
    mockMvc.perform(get("/api/users/abc"))
           .andExpect(status().isBadRequest());
  }

  @Test
  void testGetUserNoId() throws Exception {
    mockMvc.perform(get("/api/users/"))
           .andExpect(status().isNotFound());
  }

  @Test
  void testGetInvalidId() throws Exception {
    mockMvc.perform(get("/api/users/0"))
           .andExpect(status().isBadRequest());
  }

  @Test
  void testUserLogin() throws Exception {
    mockMvc.perform(post("/api/users/login")
           .contentType("application/json")
           .content("{\"username\":\"tester\",\"password\":\"123\"}"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.token").isNotEmpty());
  }

  @Test
  void testUserRegister() throws Exception {
    mockMvc.perform(get("/api/register"))
           .andExpect(status().isOk());
  }

  @Test
  void testProtectedEndpointWithoutAuth() throws Exception {
    mockMvc.perform(get("/api/delete"))
           .andExpect(status().isForbidden());
  }
    
  @Test
  void testProtectedEndpointWithAuth() throws Exception {
    String token = tokenProvider.generateToken(1, "testuser", TEST_EXPIRATION);
    mockMvc.perform(get("/api/delete").header("Authorization", "Bearer " + token))
           .andExpect(status().isOk());
  }

  @Test
  void testProtectedEndpointWithInvalidAuth() throws Exception {
    mockMvc.perform(get("/api/delete").header("Authorization", "Bearer invalidtoken"))
           .andExpect(status().isForbidden());
  }

  @Test
  void testGetUserDetailsNonExistentUser() throws Exception {
    mockMvc.perform(get("/api/users/999/details"))
           .andExpect(status().isNotFound());
  }
}
