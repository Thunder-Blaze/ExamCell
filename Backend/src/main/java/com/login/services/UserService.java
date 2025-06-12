package com.login.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import com.login.models.UserEntity;
import com.login.models.OtpUtil;
import com.login.models.JwtUtil;
import com.login.models.JwtResponse;
import com.login.repositories.UserRepository;

@Service
public class UserService {

    private static final Pattern IIITL_EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@iiitl\\.ac\\.in$");
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean sendLoginOtp(String email) {
        if (!IIITL_EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email must end with @iiitl.ac.in");
        }
        
        String otp = OtpUtil.generateOtp();
        
        UserEntity userEntity = userRepository.findById(email)
                                .orElse(new UserEntity());
        userEntity.setEmailId(email);
        userEntity.setOtp(otp);
        userEntity.setOtpCreatedAt(LocalDateTime.now());
        
        userRepository.save(userEntity);
        emailService.sendOtpEmail(email, otp);
        return true;
    }
    
    public JwtResponse authenticateUserWithOtp(String email, String otp) {
        if (!IIITL_EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Email must end with @iiitl.ac.in");
        }
        
        Optional<UserEntity> userEntityOpt = userRepository.findById(email);
        
        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            
            if (userEntity.isOtpExpired()) {
                return new JwtResponse(null, email, "OTP has expired. Please request a new one.");
            }
            
            if (userEntity.getOtp() != null && userEntity.getOtp().equals(otp)) {
                userRepository.delete(userEntity);
                
                String token = jwtUtil.generateToken(email);
                emailService.sendWelcomeEmail(email);
                return new JwtResponse(token, email, "Authentication successful!");
                
            } else {
                return new JwtResponse(null, email, "Invalid OTP!");
            }
        } else {
            return new JwtResponse(null, email, "User not found! Please request an OTP first.");
        }
    }

    public boolean changePassword(String email, String currentPassword, String newPassword) {
        if (!IIITL_EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }
        
        Optional<UserEntity> userEntityOpt = userRepository.findById(email);
        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            if (userEntity.getPassword() == null) {
                userEntity.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(userEntity);
                return true;
            }
            if (passwordEncoder.matches(currentPassword, userEntity.getPassword())) {
                userEntity.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(userEntity);
                return true;
            }
            throw new IllegalArgumentException("Current password is incorrect");
        }
        throw new IllegalArgumentException("User not found");
    }
}