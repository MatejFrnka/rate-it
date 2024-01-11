package it.rate.webapp.services;

import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingService {
  private final RatingRepository ratingRepository;
  private final CriterionService criterionService;

  public RatingsDTO getUsersRatingsDto(AppUser appUser, Place place) {
    List<Rating> ratings = ratingRepository.findAllByAppUserAndPlace(appUser, place);
    return new RatingsDTO(ratings);
  }

  public void updateRating(RatingsDTO rating, Place place, AppUser appUser) {
    rating
        .ratings()
        .forEach(
            (key, value) -> {
              // todo: validate value to be between 0 and 10
              Criterion criterion = getCriterion(key);
              Optional<Rating> optRating =
                  ratingRepository.findById(
                      new RatingId(appUser.getId(), place.getId(), criterion.getId()));
              if (optRating.isPresent()) {
                if (value == null) {
                  ratingRepository.deleteById(
                      new RatingId(appUser.getId(), place.getId(), criterion.getId()));
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

  private Criterion getCriterion(Long criterionId) {
    Optional<Criterion> optCriterion = criterionService.findById(criterionId);
    if (optCriterion.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument");
    }
    return optCriterion.get();
  }
}
