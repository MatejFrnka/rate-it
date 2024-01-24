package it.rate.webapp.dtos;

import it.rate.webapp.models.Interest;

import java.util.List;

public record InterestSuggestionDTO(
    Long id, String name, Long likes, Double distanceKm, String imageName, List<Long> categoryIds) {

  public InterestSuggestionDTO(Interest interest, Double distanceKm) {
    this(
        interest.getId(),
        interest.getName(),
        (long) interest.countLikes(),
        distanceKm,
        interest.getImageName(),
        interest.getCategoryIds());
  }
}
