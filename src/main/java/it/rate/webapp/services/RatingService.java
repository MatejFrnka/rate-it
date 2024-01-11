package it.rate.webapp.services;

import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RatingService {
  private final RatingRepository ratingRepository;
  private final CriterionRepository criterionRepository;

  public RatingsDTO getUsersRatingsDto(AppUser appUser, Place place) {
    List<Rating> ratings = ratingRepository.findAllByAppUserAndPlace(appUser, place);
    return new RatingsDTO(ratings);
  }

  public void updateRating(RatingsDTO ratings, Place place, AppUser appUser) {
    validateRatings(ratings, place);
    ratings
        .ratings()
        .forEach(
            (key, value) -> {
              Criterion criterion = getCriterion(key);
              RatingId ratingId = new RatingId(appUser.getId(), place.getId(), criterion.getId());
              Optional<Rating> optRating = ratingRepository.findById(ratingId);
              if (optRating.isPresent()) {
                if (value == null) {
                  ratingRepository.deleteById(ratingId);
                  return;
                }
                Rating existingRating = optRating.get();
                existingRating.setScore(value);
                ratingRepository.save(existingRating);
              } else {
                if (value == null) {
                  return;
                }
                Rating newRating = new Rating(appUser, place, criterion, value);
                ratingRepository.save(newRating);
              }
            });
  }

  private void validateRatings(RatingsDTO ratings, Place place) {
    List<Criterion> placeCriteria = place.getInterest().getCriteria();
    Set<Criterion> ratedCriteria =
        ratings.ratings().keySet().stream().map(this::getCriterion).collect(Collectors.toSet());
    if (!ratedCriteria.containsAll(placeCriteria)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid criteria in ratings");
    }
    ratings
        .ratings()
        .forEach(
            (key, value) -> {
              if (value != null && (value < 1 || value > 10)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rating value");
              }
            });
  }

  private Criterion getCriterion(Long criterionId) {
    Optional<Criterion> optCriterion = criterionRepository.findById(criterionId);
    if (optCriterion.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Criterion not found");
    }
    return optCriterion.get();
  }
}
