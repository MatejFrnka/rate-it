package it.rate.webapp.dtos;

import java.util.List;

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
    Double worstRatedCriterionRating,
    List<String> imageNames) {
}

