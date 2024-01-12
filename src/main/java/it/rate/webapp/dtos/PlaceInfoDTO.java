package it.rate.webapp.dtos;

import it.rate.webapp.models.Place;

public record PlaceInfoDTO(
    Long id,
    String name,
    String address,
    double averageRating,
    double bestRatedCriterion,
    double worstRatedCriterion) {

  public PlaceInfoDTO(
      Place place,
      CriterionAvgRatingDTO bestRatedCriterion,
      CriterionAvgRatingDTO worstRatedCriterion) {
    this(
        place.getId(),
        place.getName(),
        place.getAddress(),
        place.getAverageRating(),
        bestRatedCriterion.avgRating(),
        worstRatedCriterion.avgRating());
  }
}
