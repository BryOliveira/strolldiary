package com.strolldiary.strolldiary_backend.modules.users.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.strolldiary.strolldiary_backend.modules.users.model.UserPrincipal;
import com.strolldiary.strolldiary_backend.modules.users.model.Users;
import com.strolldiary.strolldiary_backend.modules.users.repo.UserRepo;

@Service
public class UserService implements UserDetailsService {
  
  private final UserRepo repo;

  public UserService(UserRepo repo) {
    this.repo = repo;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users user = repo.findByUsername(username);

    if (user == null) {
      throw new UsernameNotFoundException("User not found");
    }

    return new UserPrincipal(user);
  }
  
}
