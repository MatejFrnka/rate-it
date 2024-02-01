package it.rate.webapp.dtos;

import it.rate.webapp.models.Place;

import java.util.List;

public record UserRatedPlaceDTO(
    Long id, String name, Double avgRating, List<UserRatingDTO> ratedCriteria) {

  public UserRatedPlaceDTO(Place place, List<UserRatingDTO> ratedCriteria) {
    this(place.getId(), place.getName(), place.getAverageRating() / 2, ratedCriteria);
  }
}
