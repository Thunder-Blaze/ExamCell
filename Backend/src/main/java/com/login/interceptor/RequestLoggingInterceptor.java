package com.login.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger("com.login.interceptor");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestId = generateRequestId();
        request.setAttribute("requestId", requestId);
        
        // Log request details
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("\n=== Request Start ===\n");
        requestLog.append("Request ID: ").append(requestId).append("\n");
        requestLog.append("Method: ").append(request.getMethod()).append("\n");
        requestLog.append("URI: ").append(request.getRequestURI()).append("\n");
        requestLog.append("Remote Address: ").append(getClientIp(request)).append("\n");
        
        // Log request headers (excluding sensitive data)
        requestLog.append("Headers:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            if (!isHeaderSensitive(headerName)) {
                requestLog.append("  ").append(headerName).append(": ")
                        .append(request.getHeader(headerName)).append("\n");
            } else {
                requestLog.append("  ").append(headerName).append(": [REDACTED]\n");
            }
        }
        requestLog.append("=== Request End ===");
        
        logger.info(requestLog.toString());
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                          ModelAndView modelAndView) {
        // This method is called after handler execution, but before view rendering
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String requestId = (String) request.getAttribute("requestId");
        
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("\n=== Response Start ===\n");
        responseLog.append("Request ID: ").append(requestId).append("\n");
        responseLog.append("Status: ").append(response.getStatus()).append("\n");
        
        // Log response headers
        responseLog.append("Headers:\n");
        for (String headerName : response.getHeaderNames()) {
            if (!isHeaderSensitive(headerName)) {
                responseLog.append("  ").append(headerName).append(": ")
                        .append(response.getHeader(headerName)).append("\n");
            } else {
                responseLog.append("  ").append(headerName).append(": [REDACTED]\n");
            }
        }
        
        // Log exception if exists
        if (ex != null) {
            responseLog.append("Exception: ").append(ex.getMessage()).append("\n");
        }
        
        responseLog.append("=== Response End ===");
        
        logger.info(responseLog.toString());
    }
    
    private String generateRequestId() {
        return String.format("%d-%d", System.currentTimeMillis(), Thread.currentThread().getId());
    }
    
    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
    
    private boolean isHeaderSensitive(String headerName) {
        return headerName.toLowerCase().contains("authorization") || 
               headerName.toLowerCase().contains("cookie") ||
               headerName.toLowerCase().contains("token");
    }
}