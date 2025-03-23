package com.example.testapispring.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class SecuredController {

    @GetMapping("/public/message")
    public ResponseEntity<Map<String, String>> publicMessage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a public endpoint");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/secure/message")
    public ResponseEntity<Map<String, String>> secureMessage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a secured endpoint with JWT");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/basic-auth/message")
    public ResponseEntity<Map<String, String>> basicAuthMessage() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "This is a secured endpoint with Basic Auth");
        return ResponseEntity.ok(response);
    }
} 