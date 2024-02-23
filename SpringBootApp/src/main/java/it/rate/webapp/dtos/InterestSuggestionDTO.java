package it.rate.webapp.dtos;

import it.rate.webapp.models.Interest;

import java.util.List;

public record InterestSuggestionDTO(
    Long id,
    String name,
    String description,
    Long likes,
    Long places,
    String imageName,
    Double distanceKm,
    List<Long> categoryIds) {

  public InterestSuggestionDTO(Interest interest, Double distanceKm) {
    this(
        interest.getId(),
        interest.getName(),
        interest.getDescription(),
        (long) interest.countLikes(),
        (long) interest.countPlaces(),
        interest.getImageName(),
        distanceKm,
        interest.getCategoryIds());
  }
}
