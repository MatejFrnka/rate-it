package it.rate.webapp.services;

import it.rate.webapp.models.*;
import it.rate.webapp.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class RatingService {
  private final RatingRepository ratingRepository;
  private final PlaceService placeService;
  private final UserService userService;
  private final CriterionService criterionService;

  public Optional<Rating> findByAppUserAndCriterionAndPlace(
      AppUser appUser, Criterion criterion, Place place) {
    return ratingRepository.findById(
        new RatingId(appUser.getId(), place.getId(), criterion.getId()));
  }

  public void updateRating(Map<String, String> rating, Long placeId, Principal principal) {
    if (principal == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }
    Optional<Place> optPlace = placeService.findById(placeId);
    Optional<AppUser> optUser = userService.findByEmail(principal.getName());
    if (optPlace.isEmpty() || optUser.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid argument");
    }
    Place place = optPlace.get();
    AppUser user = optUser.get();
    rating.forEach(
        (key, value) -> {
          Optional<Criterion> optCriterion = criterionService.findById(Long.valueOf(key));
          if (optCriterion.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Criterion id");
          }
          Criterion criterion = optCriterion.get();

          Optional<Rating> optRating =
              ratingRepository.findById(
                  new RatingId(user.getId(), place.getId(), criterion.getId()));
          if (optRating.isEmpty()) {
            Rating newRating = new Rating(user, place, criterion, Integer.parseInt(value));
            ratingRepository.save(newRating);
          } else {
            Rating existingRating = optRating.get();
            existingRating.setScore(Integer.parseInt(value));
            ratingRepository.save(existingRating);
          }
        });
  }
}
