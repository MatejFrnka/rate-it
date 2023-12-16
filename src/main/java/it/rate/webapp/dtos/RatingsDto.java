package it.rate.webapp.dtos;

import it.rate.webapp.models.Rating;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public record RatingsDto (Map<Long, Integer> ratings){
        public RatingsDto(List<Rating> ratings) {
        this(ratings.stream().collect(Collectors.toMap(Rating::getCriterionId, Rating::getScore)));
    }

    public RatingsDto() {
        this(new HashMap<>());
    }
    //thymeleaf does not like when this constructor is missing
}
