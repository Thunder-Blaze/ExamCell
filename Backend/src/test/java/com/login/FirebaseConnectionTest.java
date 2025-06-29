package com.login;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class FirebaseConnectionTest {

    @Test
    public void testFirebaseConnection() {
        try {
            // Check if Firebase is initialized
            if (!FirebaseApp.getApps().isEmpty()) { 
                // Test Firebase Messaging
                FirebaseMessaging messaging = FirebaseMessaging.getInstance();    
            } 
        } catch (Exception e) {
            System.err.println("Firebase connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 