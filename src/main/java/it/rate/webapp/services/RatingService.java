package it.rate.webapp.services;

import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.RatingRepository;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@AllArgsConstructor
public class RatingService {

  private final Validator validator;
  private final RatingRepository ratingRepository;
  private final CriterionRepository criterionRepository;

  public RatingsDTO getUsersRatingsDto(AppUser appUser, Place place) {
    List<Rating> ratings = ratingRepository.findAllByAppUserAndPlace(appUser, place);
    return new RatingsDTO(ratings);
  }

  public void updateRating(RatingsDTO ratings, Place place, AppUser appUser) {
    Set<Criterion> ratedCriteria = validateRatings(ratings, place);

    ratings
        .ratings()
        .forEach(
            (key, value) -> {
              Criterion criterion =
                  ratedCriteria.stream()
                      .filter(c -> Objects.equals(c.getId(), key))
                      .findAny()
                      .orElseThrow(
                          () ->
                              new ResponseStatusException(
                                  HttpStatus.BAD_REQUEST, "Invalid criterion in ratings"));
              updateOrCreateRating(appUser, place, criterion, value);
            });
  }

  private void updateOrCreateRating(
      AppUser appUser, Place place, Criterion criterion, Integer value) {
    RatingId ratingId = new RatingId(appUser.getId(), place.getId(), criterion.getId());
    Optional<Rating> optRating = ratingRepository.findById(ratingId);

    if (value == null) {
      if (optRating.isPresent()) {
        ratingRepository.deleteById(ratingId);
      }
      return;
    }

    if (optRating.isPresent()) {
      Rating existingRating = optRating.get();
      existingRating.setScore(value);
      ratingRepository.save(existingRating);
    } else {
      Rating newRating = new Rating(appUser, place, criterion, value);
      ratingRepository.save(newRating);
    }
  }

  private Set<Criterion> validateRatings(RatingsDTO ratings, Place place) {
    List<Criterion> placeCriteria = place.getInterest().getCriteria();
    Set<Criterion> ratedCriteria = new HashSet<>();

    ratings
        .ratings()
        .forEach(
            (key, value) -> {
              Criterion criterion = getCriterion(key);
              ratedCriteria.add(criterion);
              if (!validator.validate(ratings).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid rating value");
              }
            });

    if (!ratedCriteria.containsAll(placeCriteria)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid criteria in ratings");
    }

    return ratedCriteria;
  }

  private Criterion getCriterion(Long criterionId) {
    Optional<Criterion> optCriterion = criterionRepository.findById(criterionId);
    return optCriterion.orElseThrow(
        () ->
            new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Criterion not found for ID: " + criterionId));
  }
}
