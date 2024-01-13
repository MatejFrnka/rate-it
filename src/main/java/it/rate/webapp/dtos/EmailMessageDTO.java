package it.rate.webapp.dtos;

import jakarta.validation.constraints.Email;

public record EmailMessageDTO(@Email String to, String subject, String text) {}
