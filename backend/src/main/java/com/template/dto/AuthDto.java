package com.template.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDto {

    // here we used record Keyword to define immutable
    // data classes for the request and response objects.
    // it automatically generates constructors, getters, equals(), hashCode(), and
    // toString() methods,
    // making the code more concise and readable.
    public record RegisterRequest(
            @Email(message = "Invalid email") @NotBlank(message = "Email is required") String email,

            @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters") String password,

            String firstName,
            String lastName) {
    }

    public record LoginRequest(
            @NotBlank String email,
            @NotBlank String password) {
    }

    public record AuthResponse(
            String token,
            String email,
            String firstName,
            String lastName,
            String role) {
    }

    public record RequestPasswordResetRequest(
            @Email(message = "Invalid email")
            @NotBlank(message = "Email is required") 
            String email) {
    }

    public record ResetPasswordRequest(
            @NotBlank(message = "Token is required") String token,

            @NotBlank(message = "Password is required") @Size(min = 8, message = "Password must be at least 8 characters") String newPassword) {
    }
}
