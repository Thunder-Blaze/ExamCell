package com.login.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.login.entity.Log;
import com.login.entity.PasswordRequest;
import com.login.repositories.LogRepository;
import com.login.repositories.PasswordReqRepository;
import com.login.repositories.StudentRepository;

@Service
public class PasswordReqService {

    @Autowired
    private PasswordReqRepository passwordReqRepository;

    @Autowired
    private LogRepository logRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Transactional
    public PasswordRequest createRequest(PasswordRequest preq) {
        Log log = new Log();
        if (passwordReqRepository.existsById(preq.getEmail())) {
            log.setMessage("Updated Password request for " + preq.getEmail());
            log.setTimestamp(LocalDateTime.now());
            log.setUser(studentRepository.getUsernameByEmail(preq.getEmail()));
            logRepository.save(log);
            return passwordReqRepository.save(preq);
        };
        log.setMessage("Password request made for " + preq.getEmail());
        log.setTimestamp(LocalDateTime.now());
        log.setUser(studentRepository.getUsernameByEmail(preq.getEmail()));
        logRepository.save(log);
        return passwordReqRepository.save(preq);
    }

    public PasswordRequest getRequestByEmail(String email) {
        return passwordReqRepository.findById(email).orElse(null);
    }

    public void deleteRequest(String email) {
        if (!passwordReqRepository.existsById(email)) {
            throw new RuntimeException("Password request does not exist for email: " + email);
        }
        passwordReqRepository.deleteById(email);
        Log log = new Log();
        log.setMessage("Deleted Password request for " + email);
        log.setTimestamp(LocalDateTime.now());
        log.setUser(studentRepository.getUsernameByEmail(email));
        logRepository.save(log);
    }
    
}
