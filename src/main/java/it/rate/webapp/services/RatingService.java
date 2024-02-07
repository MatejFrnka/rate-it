package it.rate.webapp.services;

import it.rate.webapp.dtos.RatingsDTO;
import it.rate.webapp.dtos.UserRatedInterestDTO;
import it.rate.webapp.dtos.UserRatedPlaceDTO;
import it.rate.webapp.exceptions.badrequest.InvalidCriterionDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidRatingException;
import it.rate.webapp.mappers.RatingMapper;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.CriterionRepository;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RatingRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Validated
@AllArgsConstructor
public class RatingService {

  private final RatingRepository ratingRepository;
  private final CriterionRepository criterionRepository;
  private final PlaceRepository placeRepository;

  public RatingsDTO getUsersRatingsDto(@Valid AppUser appUser, @Valid Place place) {
    List<Rating> ratings = ratingRepository.findAllByAppUserAndPlace(appUser, place);
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

    if (optRating.isPresent()) {
      Rating existingRating = optRating.get();
      existingRating.setRating(rating);
      ratingRepository.save(existingRating);
    } else {
      Rating newRating = new Rating(appUser, place, criterion, rating);
      ratingRepository.save(newRating);
    }
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

  public List<UserRatedInterestDTO> getAllUserRatedInterestDTOS(AppUser appUser) {
    List<Rating> userRatings = ratingRepository.findAllByAppUser(appUser);

    Map<Interest, Map<Place, List<Rating>>> ratingsByInterestAndPlace =
        userRatings.stream()
            .filter(rating -> !rating.getCriterion().isDeleted())
            .collect(
                Collectors.groupingBy(
                    rating -> rating.getPlace().getInterest(),
                    Collectors.groupingBy(Rating::getPlace, Collectors.toList())));

    return ratingsByInterestAndPlace.entrySet().stream()
        .map(
            interest ->
                RatingMapper.remapToUserRatedInterestDTO(
                    interest.getKey(),
                    interest.getValue().entrySet().stream()
                        .map(
                            place ->
                                RatingMapper.remapToUserRatedPlaceDTO(
                                    place.getKey(), place.getValue()))
                        .sorted(Comparator.comparingDouble(UserRatedPlaceDTO::avgRating).reversed())
                        .collect(Collectors.toList())))
        .sorted(Comparator.comparingInt(UserRatedInterestDTO::likes).reversed())
        .toList();
  }

  public UserRatedInterestDTO getUserRatedInterestDTO(AppUser appUser, Interest interest) {
    List<Rating> userRatings = ratingRepository.findAllByAppUserAndInterestId(appUser, interest);
    Map<Place, List<Rating>> ratingsByPlace =
        userRatings.stream()
            .filter(rating -> !rating.getCriterion().isDeleted())
            .collect(Collectors.groupingBy(Rating::getPlace));

    return new UserRatedInterestDTO(
        interest,
        ratingsByPlace.entrySet().stream()
            .map(place -> RatingMapper.remapToUserRatedPlaceDTO(place.getKey(), place.getValue()))
            .sorted(Comparator.comparingDouble(UserRatedPlaceDTO::avgRating).reversed())
            .collect(Collectors.toList()));
  }

  public boolean existsByUserAndPlace(AppUser appUser, Place place) {
    return ratingRepository.existsByAppUserAndPlace(appUser, place);
  }
}
