package it.rate.webapp.dtos;

import it.rate.webapp.models.Place;

public record PlaceInfoDTO(
    Long id,
    String name,
    String address,
    Double latitude,
    Double longitude,
    Double avgRating,
    Long interestId,
    String bestRatedCriterionName,
    Double bestRatedCriterionScore,
    String worstRatedCriterionName,
    Double worstRatedCriterionScore) {

  public PlaceInfoDTO(
      Place place,
      CriterionAvgRatingDTO bestRatedCriterion,
      CriterionAvgRatingDTO worstRatedCriterion) {
    this(
        place.getId(),
        place.getName(),
        place.getAddress(),
        place.getLatitude(),
        place.getLongitude(),
        place.getAverageRating(),
        place.getInterest().getId(),
        bestRatedCriterion.name(),
        bestRatedCriterion.avgRating(),
        worstRatedCriterion.name(),
        worstRatedCriterion.avgRating());
  }
}
