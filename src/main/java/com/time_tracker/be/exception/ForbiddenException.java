package com.time_tracker.be.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Getter
@Component
public class ForbiddenException extends RuntimeException implements AccessDeniedHandler {
    private final int statusCode;
    private final LocalDateTime timestamp;

    public ForbiddenException(String message) {
        super(message);
        this.statusCode = 403;
        this.timestamp = LocalDateTime.now();
    }

    public ForbiddenException() {
        this.statusCode = 403;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(this.statusCode);  // Set the status code to 403 (Forbidden)
        response.getWriter().write("Access Denied: You do not have permission to access this resource."); // Provide a message in the response body
    }
}
