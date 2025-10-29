package com.vandana.dto.request;

import jakarta.validation.constraints.*;

import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must have at least 8 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "OWNER|WORKER", message = "Role must be either OWNER or WORKER")
    private String role;  // OWNER or WORKER
}