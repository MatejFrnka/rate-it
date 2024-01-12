package it.rate.webapp.dtos;
import org.hibernate.validator.constraints.Range;

public record CoordinatesDTO(
        @Range(min = -90, max = 90) double latitude, @Range(min = -180, max = 180) double longitude) {}
