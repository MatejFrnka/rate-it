package it.rate.webapp.services;

import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.exceptions.badrequest.InvalidCriterionDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidRatingException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.RatingRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;

@Service
@Validated
@AllArgsConstructor
public class RatingService {

  private final RatingRepository ratingRepository;
  private final CriterionRepository criterionRepository;

  public List<Rating> findAllByAppUserAndPlace(@Valid AppUser appUser, @Valid Place place) {
    return ratingRepository.findAllByAppUserAndPlaceAndCriterionDeletedFalse(appUser, place);
  }

  public List<Rating> findAllByCriterionAndPlace(@Valid Criterion criterion, @Valid Place place) {
    return ratingRepository.findAllByCriterionAndPlaceAndCriterionDeletedFalse(criterion, place);
  }

  public RatingsDTO getUsersRatingsDto(@Valid AppUser appUser, @Valid Place place) {
    List<Rating> ratings =
        ratingRepository.findAllByAppUserAndPlaceAndCriterionDeletedFalse(appUser, place);
    return new RatingsDTO(ratings);
  }

  public void save(@NotNull @Valid RatingsDTO ratings, @Valid Place place, @Valid AppUser appUser) {
    Set<Criterion> ratedCriteria = validateRatings(ratings, place);

    ratings
        .ratings()
        .forEach(
            (criterionId, rating) -> {
              Criterion criterion =
                  ratedCriteria.stream()
                      .filter(c -> Objects.equals(c.getId(), criterionId))
                      .findAny()
                      .orElseThrow(InvalidCriterionDetailsException::new);
              updateOrCreateRating(appUser, place, criterion, rating);
            });
  }

  private void updateOrCreateRating(
      AppUser appUser, Place place, Criterion criterion, Integer rating) {
    RatingId ratingId = new RatingId(appUser.getId(), place.getId(), criterion.getId());
    Optional<Rating> optRating = ratingRepository.findById(ratingId);

    if (rating == null) {
      if (optRating.isPresent()) {
        ratingRepository.deleteById(ratingId);
      }
      return;
    }

    optRating.ifPresent(value -> ratingRepository.deleteById(value.getId()));
    Rating newRating = new Rating(appUser, place, criterion, rating);
    ratingRepository.save(newRating);
  }

  private Set<Criterion> validateRatings(RatingsDTO ratings, Place place) {
    List<Criterion> placeCriteria = place.getInterest().getCriteria();
    Set<Criterion> ratedCriteria = new HashSet<>();

    ratings
        .ratings()
        .forEach(
            (criterionId, rating) -> {
              Criterion criterion = getCriterion(criterionId);
              ratedCriteria.add(criterion);
            });
    if (!ratedCriteria.containsAll(placeCriteria)) {
      throw new InvalidRatingException();
    }

    return ratedCriteria;
  }

  private Criterion getCriterion(Long criterionId) {
    return criterionRepository
        .findById(criterionId)
        .orElseThrow(InvalidCriterionDetailsException::new);
  }
}
