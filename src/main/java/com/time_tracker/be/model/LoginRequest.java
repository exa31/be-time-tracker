package com.time_tracker.be.model;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "Email should be valid")
    private String email;

    private String password;
    private String idToken;

}
