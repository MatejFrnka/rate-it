package it.rate.webapp.dtos;

import it.rate.webapp.models.Interest;

import java.util.List;

public record RatedInterestDTO(
    Long interestId, String interestName, String imageName, List<PlaceReviewDTO> ratedPlaces) {
  public RatedInterestDTO(Interest interest, List<PlaceReviewDTO> ratedPlaces) {
    this(interest.getId(), interest.getName(), interest.getImageName(), ratedPlaces);
  }
}
