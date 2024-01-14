package it.rate.webapp.dtos;

import it.rate.webapp.models.Place;

public record PlaceInfoDTO(
    Long id,
    String name,
    String address,
    Double latitude,
    Double longitude,
    Double avgRating,
    String bestRatedCriterionName,
    Double bestRatedCriterionRating,
    String worstRatedCriterionName,
    Double worstRatedCriterionRating) {

  public PlaceInfoDTO(
      Place place, CriterionAvgRatingDTO bestCriterion, CriterionAvgRatingDTO worstCriterion) {
    this(
        place.getId(),
        place.getName(),
        place.getAddress(),
        place.getLatitude(),
        place.getLongitude(),
        place.getAverageRating(),
        bestCriterion.name(),
        bestCriterion.avgRating(),
        worstCriterion.name(),
        worstCriterion.avgRating());
  }
}
