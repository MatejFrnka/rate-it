package it.rate.webapp.dtos;

public record PlaceInfoDTO(
    Long id,
    String name,
    String address,
    double latitude,
    double longitude,
    double avgRating,
    CriterionAvgRatingDTO bestRatedCriterion,
    CriterionAvgRatingDTO worstRatedCriterion) {}
