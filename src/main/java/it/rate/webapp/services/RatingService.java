package it.rate.webapp.services;

import it.rate.webapp.models.*;
import it.rate.webapp.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingService {
  private RatingRepository ratingRepository;

  public Optional<Rating> findByAppUserAndCriterionAndPlace(
      AppUser appUser, Criterion criterion, Place place) {
    return ratingRepository.findById(
        new RatingId(appUser.getId(), place.getId(), criterion.getId()));
  }
}
