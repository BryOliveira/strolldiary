package com.strolldiary.strolldiary_backend.config.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.strolldiary.strolldiary_backend.common.sec.TokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class.getName());

  private final TokenProvider tokenProvider;

  public JwtAuthenticationFilter(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request, 
      @NonNull HttpServletResponse response, 
      @NonNull FilterChain filterChain
    )
      throws ServletException, IOException {
    try {
      String authHeader = request.getHeader("Authorization");
      if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        if (tokenProvider.validateToken(token)) {
          String userId = tokenProvider.getUserIDFromToken(token);
          if (userId != null) {
            request.setAttribute("userId", Integer.parseInt(userId));
          }
        }
      }

      filterChain.doFilter(request, response);
    } catch (Exception e) {
      LOGGER.warn("JWT authentication error: {}", e.getMessage());
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}
