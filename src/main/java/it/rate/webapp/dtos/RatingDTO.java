package it.rate.webapp.dtos;

import it.rate.webapp.models.Rating;

public record RatingDTO(int rating, String criterionName) {
  public RatingDTO(Rating rating) {
    this(rating.getRating(), rating.getCriterion().getName());
  }
}
