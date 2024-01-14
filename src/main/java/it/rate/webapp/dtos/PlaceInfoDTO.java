package it.rate.webapp.dtos;

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
}
