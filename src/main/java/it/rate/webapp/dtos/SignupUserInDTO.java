package it.rate.webapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SignupUserInDTO(
    @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email address")
        String email,
    @NotBlank(message = "Password cannot be empty")
        @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$",
            message =
                "Password needs to be at least 8 characters long and contain at least one uppercase letter and one digit")
        String password,
    @NotBlank(message = "Username cannot be empty") String username) {}
