package it.rate.webapp.services;

import it.rate.webapp.models.*;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
    Optional<Place> optPlace = placeService.findById(placeId);
    Optional<AppUser> optUser = userService.findByEmail(principal.getName());
    if (optPlace.isEmpty() || optUser.isEmpty()) {
      throw new RuntimeException("User or place not found");
    }
    Place place = optPlace.get();
    AppUser user = optUser.get();
    rating.entrySet().stream()
        .forEach(
            e -> {
              Optional<Criterion> optCriterion = criterionService.findById(Long.valueOf(e.getKey()));
              if (optCriterion.isEmpty()) {
                throw new RuntimeException("Criterion not found");
              }
              Criterion criterion = optCriterion.get();

              Optional<Rating> optRating =
                  ratingRepository.findById(
                      new RatingId(user.getId(), place.getId(), criterion.getId()));
              if (optRating.isEmpty()) {
                Rating newRating = new Rating(user, place, criterion, Integer.parseInt(e.getValue()));
                ratingRepository.save(newRating);
              } else {
                Rating existingRating = optRating.get();
                existingRating.setScore(Integer.parseInt(e.getValue()));
                ratingRepository.save(existingRating);
              }
            });
  }
}
