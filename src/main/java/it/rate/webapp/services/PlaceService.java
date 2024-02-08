package it.rate.webapp.services;

import it.rate.webapp.dtos.*;
import it.rate.webapp.models.*;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.repositories.PlaceRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class PlaceService {

  private final PlaceRepository placeRepository;
  private final RatingService ratingService;
  private final ReviewService reviewService;
  private final UserService userService;

  public Optional<Place> findById(Long id) {
    return placeRepository.findById(id);
  }

  public Place getById(Long placeId) {
    return placeRepository.getReferenceById(placeId);
  }

  public Place save(
      @NotNull @Valid PlaceInDTO placeDTO, @Valid Interest interest, @Valid AppUser appUser) {
    Place place = new Place(placeDTO);
    place.setCreator(appUser);
    place.setInterest(interest);

    return placeRepository.save(place);
  }

  public Place update(@Valid Place place, @NotNull @Valid PlaceInDTO placeDTO) {
    place.update(placeDTO);
    return placeRepository.save(place);
  }

  public void addImage(@Valid Place place, String imageId) {
    place.getImageNames().add(imageId);
    placeRepository.save(place);
  }

  public List<PlaceInfoDTO> getPlaceInfoDTOS(@Valid Interest interest) {
    return interest.getPlaces().stream().map(this::getPlaceInfoDTO).collect(Collectors.toList());
  }

  public CriteriaOfPlaceDTO getCriteriaOfPlaceDTO(@Valid Place place) {
    List<CriterionAvgRatingDTO> criteriaAvgRatingDTOs = new ArrayList<>();

    place
        .getInterest()
        .getCriteria()
        .forEach(
            criterion -> criteriaAvgRatingDTOs.add(getCriterionAvgRatingDTO(criterion, place)));

    return new CriteriaOfPlaceDTO(criteriaAvgRatingDTOs);
  }

  public PlaceInfoDTO getPlaceInfoDTO(@Valid Place place) {
    Set<CriterionAvgRatingDTO> criteria =
        place.getInterest().getCriteria().stream()
            .map(criterion -> getCriterionAvgRatingDTO(criterion, place))
            .collect(Collectors.toSet());
    return new PlaceInfoDTO(place, criteria);
  }

  private CriterionAvgRatingDTO getCriterionAvgRatingDTO(Criterion criterion, Place place) {
    List<Rating> ratings = ratingService.findAllByCriterionAndPlace(criterion, place);
    Double avgRating = calculateAverageRating(ratings);

    return new CriterionAvgRatingDTO(criterion.getId(), criterion.getName(), avgRating);
  }

  public List<PlaceReviewDTO> getPlaceReviewDTOs(
      @Valid AppUser user, @Valid Interest interest, Comparator<PlaceReviewDTO> comparator) {
    List<Place> distinctPlaces =
        placeRepository.findAllDistinctByAppUserAndInterest(user, interest);

    return distinctPlaces.stream()
        .map(place -> getPlaceReviewDTO(user, place))
        .sorted(comparator)
        .toList();
  }

  public List<PlaceReviewDTO> getPlaceReviewDTOs(@Valid Place place) {
    List<AppUser> appUsers = userService.findAllDistinctByPlace(place);

    return appUsers.stream()
        .map(appUser -> getPlaceReviewDTO(appUser, place))
        .sorted(Comparator.comparing(PlaceReviewDTO::timestamp).reversed())
        .toList();
  }

  private PlaceReviewDTO getPlaceReviewDTO(AppUser user, Place place) {
    Optional<Review> optReview = reviewService.findById(new ReviewId(user.getId(), place.getId()));
    String review = optReview.map(Review::getText).orElse(null);
    List<Rating> ratings = ratingService.findAllByAppUserAndPlace(user, place);
    List<RatingDTO> ratingDTOS = ratings.stream().map(RatingDTO::new).toList();
    Double avgRating = calculateAverageRating(ratings);
    Timestamp latestTimestamp = findLatestTimestamp(optReview, ratings);

    return new PlaceReviewDTO(
        user.getUsername(),
        place.getName(),
        place.getId(),
        review,
        ratingDTOS,
        avgRating,
        latestTimestamp);
  }

  private Double calculateAverageRating(List<Rating> ratings) {
    return ratings.stream().mapToDouble(Rating::getRating).average().orElse(Double.NaN);
  }

  private Timestamp findLatestTimestamp(Optional<Review> optReview, List<Rating> ratings) {
    List<Timestamp> timestamps =
        new ArrayList<>(ratings.stream().map(Rating::getCreatedAt).toList());
    optReview.ifPresent(value -> timestamps.add(value.getCreatedAt()));
    return timestamps.stream().max(Comparator.naturalOrder()).orElse(null);
  }
}
