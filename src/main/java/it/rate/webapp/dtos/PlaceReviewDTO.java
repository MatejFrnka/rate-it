package it.rate.webapp.dtos;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import java.sql.Timestamp;
import java.util.List;

public record PlaceReviewDTO(
    String userName,
    Long userId,
    String placeName,
    Long placeId,
    Long interestId,
    String review,
    List<RatingDTO> ratings,
    Double avgRating,
    Timestamp timestamp) {

  public PlaceReviewDTO(
      AppUser appUser,
      Place place,
      String review,
      List<RatingDTO> ratings,
      Double avgRating,
      Timestamp timestamp) {
    this(
        appUser.getUsername(),
        appUser.getId(),
        place.getName(),
        place.getId(),
        place.getInterest().getId(),
        review,
        ratings,
        avgRating,
        timestamp);
  }
}
