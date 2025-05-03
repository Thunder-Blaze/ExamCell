package com.login.controllers;

import com.login.models.LogEntry;
import com.login.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @PostMapping
    public ResponseEntity<LogEntry> createLog(
            @RequestParam String serviceName,
            @RequestParam String logLevel,
            @RequestParam String message) {
        LogEntry logEntry = logService.saveLog(serviceName, logLevel, message);
        return ResponseEntity.ok(logEntry);
    }

    @GetMapping
    public ResponseEntity<List<LogEntry>> getAllLogs() {
        List<LogEntry> logs = logService.getAllLogs();
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/service/{serviceName}")
    public ResponseEntity<List<LogEntry>> getLogsByService(@PathVariable String serviceName) {
        List<LogEntry> logs = logService.getLogsByService(serviceName);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/level/{logLevel}")
    public ResponseEntity<List<LogEntry>> getLogsByLevel(@PathVariable String logLevel) {
        List<LogEntry> logs = logService.getLogsByLevel(logLevel);
        return ResponseEntity.ok(logs);
    }
}
