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
                System.out.println("Firebase is initialized successfully!");
                
                // Test Firebase Messaging
                FirebaseMessaging messaging = FirebaseMessaging.getInstance();
                System.out.println("Firebase Messaging is available!");
                
            } else {
                System.out.println("Firebase is not initialized!");
            }
        } catch (Exception e) {
            System.err.println("Firebase connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 