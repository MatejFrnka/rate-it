package it.rate.webapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailMessageDTO(
    @NotBlank(message = "Email cannot be empty") @Email(message = "Invalid email address")
        String from,
    @NotBlank(message = "Subject cannot be empty") String subject,
    @NotBlank(message = "Message cannot be empty") String text) {}
