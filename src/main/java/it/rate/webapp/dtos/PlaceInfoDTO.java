package it.rate.webapp.dtos;

import it.rate.webapp.models.Place;

public record PlaceInfoDTO(
    Place place,
    CriterionAvgRatingDTO bestRatedCriterion,
    CriterionAvgRatingDTO worstRatedCriterion) {}
