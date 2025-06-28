package com.login.controllers;

import com.login.services.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private FirebaseService firebaseService;

    @PostMapping("/send-notification")
    public ResponseEntity<?> sendTestNotification(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");
            String title = request.get("title");
            String body = request.get("body");
            
            if (token == null || title == null || body == null) {
                return ResponseEntity.badRequest().body(Map.of(
                    "status", "error", 
                    "message", "token, title, and body are required"
                ));
            }
            
            boolean result = firebaseService.sendTestNotification(token, title, body);
            
            if (result) {
                return ResponseEntity.ok(Map.of(
                    "status", "success", 
                    "message", "Test notification sent successfully"
                ));
            } else {
                return ResponseEntity.status(500).body(Map.of(
                    "status", "error", 
                    "message", "Failed to send test notification"
                ));
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "status", "error", 
                "message", "Error: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "success",
            "message", "Firebase notification service is running"
        ));
    }
} 