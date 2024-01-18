package it.rate.webapp.dtos;

import it.rate.webapp.models.Place;

import java.util.List;
import java.util.Set;

public record PlaceInfoDTO(
    Long id,
    String name,
    String address,
    Double latitude,
    Double longitude,
    List<String> imageNames,
    Double avgRating,
    Set<CriterionAvgRatingDTO> criteria) {

  public PlaceInfoDTO(Place place, Set<CriterionAvgRatingDTO> criteria) {
    this(
        place.getId(),
        place.getName(),
        place.getAddress(),
        place.getLatitude(),
        place.getLongitude(),
        place.getImageNames(),
        place.getAverageRating(),
        criteria);
  }
}
