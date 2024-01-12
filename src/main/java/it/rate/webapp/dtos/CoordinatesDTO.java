package it.rate.webapp.dtos;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record CoordinatesDTO(
    @NotNull(message = "Latitude cannot be null")
        @Range(min = -90, max = 90, message = "Latitude must be between -90 and 90")
        Double latitude,
    @NotNull(message = "Longitude cannot be null")
        @Range(min = -180, max = 180, message = "Longitude must be between -180 and 180")
        Double longitude) {}
