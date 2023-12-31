package it.rate.webapp.dtos;

import it.rate.webapp.models.Interest;

public record InterestSuggestionDTO(Long id, String name, Long rating) {

  public InterestSuggestionDTO(Interest interest) {
    this(interest.getId(), interest.getName(), (long) interest.countLikes());
  }
}
