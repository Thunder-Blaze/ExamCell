package com.login;

import com.login.entity.Student;
import com.login.services.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DatabaseTest {

    @Autowired
    private StudentService studentService;

    @Test
    public void testStudentDatabase() {
        try {
            System.out.println("=== DATABASE TEST ===");
            
            // Test finding a student
            String testEmail = "lit2024037@iiitl.ac.in";
            Student student = studentService.getStudentByEmail(testEmail);
            
            if (student != null) {
                System.out.println("Student found:");
                System.out.println("- Email: " + student.getEmail());
                System.out.println("- Name: " + student.getFullName());
                System.out.println("- Roll: " + student.getRollNumber());
                System.out.println("- Firebase Token: " + (student.getFirebaseToken() != null ? "Present" : "NULL"));
            } else {
                System.out.println("Student not found for email: " + testEmail);
            }
            
            // Test updating Firebase token
            if (student != null) {
                System.out.println("Testing Firebase token update...");
                student.setFirebaseToken("test-firebase-token-123");
                Student updatedStudent = studentService.updateStudent(student);
                System.out.println("Firebase token updated: " + updatedStudent.getFirebaseToken());
            }
            
        } catch (Exception e) {
            System.err.println("Database test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 