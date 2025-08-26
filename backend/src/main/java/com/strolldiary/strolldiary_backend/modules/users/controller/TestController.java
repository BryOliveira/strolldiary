package com.strolldiary.strolldiary_backend.modules.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.strolldiary.strolldiary_backend.modules.users.model.Users;
import com.strolldiary.strolldiary_backend.modules.users.repo.UserRepo;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    private final UserRepo userRepo;

    public TestController(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @GetMapping("/api/test")
    public ResponseEntity<String> test() {
        Users testerUser = userRepo.findByUsername("tester");
        Users user1User = userRepo.findByUsername("user1");

        if (testerUser != null && user1User != null) {
            return ResponseEntity.ok("Users found: " + testerUser.getUsername() + ", " + user1User.getUsername());
        } else {
            return ResponseEntity.ok("User not found.");
        }

    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the Stroll Diary Backend!");
    }
}