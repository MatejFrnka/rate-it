package it.rate.webapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignupUserInDTO(
    @Email String email, @NotBlank String password, @NotBlank String username) {}
