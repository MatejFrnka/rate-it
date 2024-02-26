package it.rate.webapp.dtos;

public record ErrorResponseDTO(int status, String simpleMessage, String clientMessage) {}
