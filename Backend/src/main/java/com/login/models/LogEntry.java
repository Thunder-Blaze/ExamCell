package com.login.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_entries")
public class LogEntry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "service_name", nullable = false)
    private String serviceName;
    
    @Column(name = "log_level", nullable = false)
    private String logLevel;
    
    @Column(nullable = false)
    private String message;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Default constructor
    public LogEntry() {
        this.timestamp = LocalDateTime.now();
    }

    // Constructor with fields
    public LogEntry(String serviceName, String logLevel, String message) {
        this.serviceName = serviceName;
        this.logLevel = logLevel;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

