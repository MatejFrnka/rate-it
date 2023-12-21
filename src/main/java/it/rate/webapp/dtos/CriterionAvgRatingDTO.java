package it.rate.webapp.dtos;

import it.rate.webapp.models.Criterion;

public record CriterionAvgRatingDTO(Criterion criterion, double avgRating) {}
