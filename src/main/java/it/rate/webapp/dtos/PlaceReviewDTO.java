package it.rate.webapp.dtos;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Place;
import java.sql.Timestamp;
import java.util.List;

public record PlaceReviewDTO(
    String userName,
    String placeName,
    Long placeId,
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
        place.getName(),
        place.getId(),
        review,
        ratings,
        avgRating,
        timestamp);
  }
}
