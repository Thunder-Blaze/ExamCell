package com.login.repositories;

import com.login.models.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    
    // Find logs by service name
    List<LogEntry> findByServiceName(String serviceName);
    
    // Find logs by log level
    List<LogEntry> findByLogLevel(String logLevel);
    
    // Find logs by service name and log level
    List<LogEntry> findByServiceNameAndLogLevel(String serviceName, String logLevel);
    
    // Find logs within a time range
    List<LogEntry> findByTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    // Find logs containing specific text in message
    List<LogEntry> findByMessageContaining(String text);
    
    // Custom query to find logs by service name and time range
    @Query("SELECT l FROM LogEntry l WHERE l.serviceName = :serviceName AND l.timestamp BETWEEN :startTime AND :endTime")
    List<LogEntry> findLogsByServiceAndTimeRange(
            @Param("serviceName") String serviceName,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );
}
