package com.login.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger("com.login.security");
    
    // Rate limit: 10 requests per minute by IP
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    
    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        return Bucket4j.builder().addLimit(limit).build();
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Skip rate limiting for non-API requests
        String path = request.getRequestURI();
        if (!path.startsWith("/api/")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // Get client IP
        String clientIp = getClientIp(request);
        
        // Get or create bucket for this IP
        Bucket bucket = buckets.computeIfAbsent(clientIp, k -> createNewBucket());
        
        // Try to consume a token
        if (bucket.tryConsume(1)) {
            // Token consumed, proceed with the request
            filterChain.doFilter(request, response);
        } else {
            // No tokens available, return 429 Too Many Requests
            logger.warn("Rate limit exceeded for IP: {}", clientIp);
            response.setStatus(429);
            response.getWriter().write("Too many requests. Please try again later.");
        }
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}