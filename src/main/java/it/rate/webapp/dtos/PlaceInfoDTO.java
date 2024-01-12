package it.rate.webapp.dtos;

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
        place.getInterest().getId(),
        place.getAverageRating(),
        bestRatedCriterion.criterion().getName(),
        bestRatedCriterion.avgRating(),
        worstRatedCriterion.criterion().getName(),
        worstRatedCriterion.avgRating());
  }
}
