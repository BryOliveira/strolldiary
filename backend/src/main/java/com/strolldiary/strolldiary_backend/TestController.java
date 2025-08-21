package com.strolldiary.strolldiary_backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    @GetMapping("/api/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Yas Reloading!");
    }

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to the Stroll Diary Backend!");
    }
}