package it.rate.webapp.dtos;

import it.rate.webapp.models.Interest;

public record InterestSuggestionDTO(Long id, String name, Long likes, Double distanceKm) {

  public InterestSuggestionDTO(Interest interest, Double distanceKm) {
    this(interest.getId(), interest.getName(), (long) interest.countLikes(), distanceKm);
  }
}
