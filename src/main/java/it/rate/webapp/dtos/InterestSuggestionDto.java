package it.rate.webapp.dtos;

import it.rate.webapp.models.Interest;

public record InterestSuggestionDto(Long id, String name, Long rating) {

  public InterestSuggestionDto(Interest interest) {
    this(interest.getId(), interest.getName(), (long) interest.countLikes());
  }
}
