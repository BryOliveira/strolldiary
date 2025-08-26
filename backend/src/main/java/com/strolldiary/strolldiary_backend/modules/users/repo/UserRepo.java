package com.strolldiary.strolldiary_backend.modules.users.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.strolldiary.strolldiary_backend.modules.users.model.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Integer>{
  Users findByUsername(String username);
}
