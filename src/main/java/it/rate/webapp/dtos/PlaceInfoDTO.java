package it.rate.webapp.dtos;

import java.util.List;

public record PlaceInfoDTO(
    Long id,
    String name,
    String address,
    Double latitude,
    Double longitude,
    Double avgRating,
    CriterionAvgRatingDTO bestRatedCriterion,
    CriterionAvgRatingDTO worstRatedCriterion,
    List<String> imageNames) {}
