package it.rate.webapp.services;

import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.exceptions.badrequest.InvalidCriterionDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidPlaceDetailsException;
import it.rate.webapp.exceptions.unauthorised.ForbiddenOperationException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingService {
  private final RatingRepository ratingRepository;
  private final PlaceService placeService;
  private final UserService userService;
  private final CriterionService criterionService;

  public RatingsDTO getUsersRatingsDto(Principal principal, Long placeId) {
    AppUser loggedUser = getLoggedUser(principal);
    Place place = getPlace(placeId);
    List<Rating> ratings = ratingRepository.findAllByAppUserAndPlace(loggedUser, place);
    return new RatingsDTO(ratings);
  }

  public void updateRating(RatingsDTO rating, Long placeId, Principal principal) {
    AppUser loggedUser = getLoggedUser(principal);
    Place place = getPlace(placeId);
    rating
        .ratings()
        .forEach(
            (key, value) -> {
              // todo: validate value to be between 0 and 10
              Criterion criterion = getCriterion(key);
              Optional<Rating> optRating =
                  ratingRepository.findById(
                      new RatingId(loggedUser.getId(), place.getId(), criterion.getId()));
              if (optRating.isPresent()) {
                if (value == null) {
                  ratingRepository.deleteById(
                      new RatingId(loggedUser.getId(), place.getId(), criterion.getId()));
                  return;
                }
                Rating existingRating = optRating.get();
                existingRating.setScore(value);
                ratingRepository.save(existingRating);
              } else {
                if (value == null) {
                  return;
                }
                Rating newRating = new Rating(loggedUser, place, criterion, value);
                ratingRepository.save(newRating);
              }
            });
  }

  private AppUser getLoggedUser(Principal principal) {
    if (principal == null) {
      throw new ForbiddenOperationException("User not authenticated");
    }
    return userService.getByEmail(principal.getName());
  }

  private Place getPlace(Long placeId) {
    return placeService.findById(placeId).orElseThrow(InvalidPlaceDetailsException::new);
  }

  private Criterion getCriterion(Long criterionId) {
    return criterionService
        .findById(criterionId)
        .orElseThrow(InvalidCriterionDetailsException::new);
  }
}
