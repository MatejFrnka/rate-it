package it.rate.webapp.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailMessageDTO(@Email String to, String subject, String text) {}
