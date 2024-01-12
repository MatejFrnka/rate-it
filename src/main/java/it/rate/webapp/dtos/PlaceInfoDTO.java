package it.rate.webapp.dtos;

import it.rate.webapp.models.Place;

public record PlaceInfoDTO(
    Long id,
    String name,
    String address,
    Long interestId,
    double averageRating,
    String bestRatedCriterionName,
    double bestRatedCriterionScore,
    String worstRatedCriterionName,
    double worstRatedCriterionScore) {

  public PlaceInfoDTO(
      Place place,
      CriterionAvgRatingDTO bestRatedCriterion,
      CriterionAvgRatingDTO worstRatedCriterion) {
    this(
        place.getId(),
        place.getName(),
        place.getAddress(),
        place.getInterest().getId(),
        place.getAverageRating(),
        bestRatedCriterion.criterion().getName(),
        bestRatedCriterion.avgRating(),
        worstRatedCriterion.criterion().getName(),
        worstRatedCriterion.avgRating());
  }
}
