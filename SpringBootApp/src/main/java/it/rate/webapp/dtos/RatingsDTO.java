package it.rate.webapp.dtos;

import it.rate.webapp.models.Rating;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RatingsDTO(Map<@NotNull Long, @Range(min = 1, max = 10) Integer> ratings) {
  public RatingsDTO(List<Rating> ratings) {
    this(ratings.stream().collect(Collectors.toMap(Rating::getCriterionId, Rating::getRating)));
  }

  public RatingsDTO() {
    this(new HashMap<>());
  }
  // thymeleaf does not like when this constructor is missing
}
