package com.strolldiary.strolldiary_backend.config.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.cors.CorsConfigurationSource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class SecurityConfigTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private SecurityFilterChain securityFilterChain;

  @Autowired
  private CorsConfigurationSource corsConfigurationSource;

  @Test
  void securityFilterChainExists() {
    assertNotNull(securityFilterChain);
  }

  @Test
  void corsConfigurationSourceExists() {
    assertNotNull(corsConfigurationSource);
  }

  @Test
  void loginEndpointIsPublic() throws Exception {
    mockMvc.perform(post("/login"))
           .andExpect(status().isOk());
  }

  @Test 
  void registerEndpointIsPublic() throws Exception {
    mockMvc.perform(post("/register"))
           .andExpect(status().isOk());
           
  }

  @Test
  void protectedEndpointRequiresAuth() throws Exception {
    mockMvc.perform(get("/protected"))
           .andExpect(status().isForbidden());
  }
}
