package com.time_tracker.be.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Getter
public class NotAuthorizedException extends RuntimeException implements AuthenticationEntryPoint {
    private final int statusCode;
    private final LocalDateTime timestamp;

    public NotAuthorizedException(String message) {
        super(message);
        this.statusCode = 401;
        this.timestamp = LocalDateTime.now();
    }

    public NotAuthorizedException() {
        this.statusCode = 401;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        // Handle Unauthorized (401) error
        response.setStatus(this.statusCode);
        response.getWriter().write("Unauthorized: You need to authenticate to access this resource.");
    }
}
