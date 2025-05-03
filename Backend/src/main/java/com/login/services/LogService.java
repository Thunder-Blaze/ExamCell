package com.login.services;

import com.login.models.LogEntry;
import com.login.repositories.LogEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogService {

    private final LogEntryRepository logEntryRepository;

    @Autowired
    public LogService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }

    /**
     * Saves a new log entry
     * @param serviceName The name of the service generating the log
     * @param logLevel The level of the log (e.g., INFO, ERROR, DEBUG)
     * @param message The log message
     * @return The saved LogEntry
     */
    public LogEntry saveLog(String serviceName, String logLevel, String message) {
        LogEntry logEntry = new LogEntry(serviceName, logLevel, message);
        return logEntryRepository.save(logEntry);
    }

    /**
     * Retrieves all log entries
     * @return List of all LogEntry objects
     */
    public List<LogEntry> getAllLogs() {
        return logEntryRepository.findAll();
    }

    /**
     * Retrieves logs by service name
     * @param serviceName The name of the service
     * @return List of LogEntry objects for the specified service
     */
    public List<LogEntry> getLogsByService(String serviceName) {
        return logEntryRepository.findByServiceName(serviceName);
    }

    /**
     * Retrieves logs by log level
     * @param logLevel The level of the log (e.g., INFO, ERROR, DEBUG)
     * @return List of LogEntry objects with the specified log level
     */
    public List<LogEntry> getLogsByLevel(String logLevel) {
        return logEntryRepository.findByLogLevel(logLevel);
    }
}
