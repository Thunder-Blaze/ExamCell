package com.login;

import com.login.services.FirebaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FirebaseNotificationTest {

    @Autowired
    private FirebaseService firebaseService;

    @Test
    public void testFirebaseNotification() {
        // This is a placeholder test - replace with actual Firebase token for testing
        String testToken = "YOUR_TEST_FIREBASE_TOKEN";
        String studentName = "Test Student";
        String enrollmentNumber = "LIT2024037";
        
        boolean result = firebaseService.sendCertificateSignedNotification(
            testToken, studentName, enrollmentNumber
        );
        
        System.out.println("Firebase notification test result: " + result);
    }
} 