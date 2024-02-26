package it.rate.webapp.dtos;

import it.rate.webapp.config.Constraints;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

public record InterestInDTO(
    @NotBlank @Length(min = Constraints.MIN_NAME_LENGTH, max = Constraints.MAX_NAME_LENGTH)
        String name,
    @NotBlank @Length(max = Constraints.MAX_DESCRIPTION_LENGTH) String description,
    @NotNull Set<@NotBlank @Length(max = Constraints.MAX_NAME_LENGTH) String> criteriaNames,
    Set<Long> categoryIds,
    boolean exclusive,
    String imageName) {}
