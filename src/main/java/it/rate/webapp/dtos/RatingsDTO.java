package it.rate.webapp.dtos;

import it.rate.webapp.models.Rating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public record RatingsDTO(Map<Long, Integer> ratings) {
  public RatingsDTO(List<Rating> ratings) {
    this(ratings.stream().collect(Collectors.toMap(Rating::getCriterionId, Rating::getScore)));
  }

  public RatingsDTO() {
    this(new HashMap<>());
  }
  // thymeleaf does not like when this constructor is missing
}
