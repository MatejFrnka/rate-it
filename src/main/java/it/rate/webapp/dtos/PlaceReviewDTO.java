package it.rate.webapp.dtos;

import java.sql.Timestamp;
import java.util.List;

public record PlaceReviewDTO(
    String userName,
    String placeName,
    Long placeId,
    String review,
    List<RatingDTO> ratings,
    Double avgRating,
    Timestamp timestamp) {}
