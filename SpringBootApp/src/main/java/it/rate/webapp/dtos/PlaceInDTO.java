package it.rate.webapp.dtos;

import it.rate.webapp.config.Constraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

public record PlaceInDTO(
    @NotBlank @Length(min = Constraints.MIN_NAME_LENGTH, max = Constraints.MAX_NAME_LENGTH)
        String name,
    @Length(max = Constraints.MAX_VARCHAR_LENGTH) String address,
    @Length(max = Constraints.MAX_DESCRIPTION_LENGTH) String description,
    @NotNull @Range(min = -90, max = 90) Double latitude,
    @NotNull @Range(min = -180, max = 180) Double longitude) {}
