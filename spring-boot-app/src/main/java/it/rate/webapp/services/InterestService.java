package it.rate.webapp.services;

import it.rate.webapp.dtos.*;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.*;
import jakarta.validation.*;

import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class InterestService {

  private final InterestRepository interestRepository;
  private final CategoryService categoryService;
  private final PlaceService placeService;

  public Optional<Interest> findById(Long interestId) {
    return interestRepository.findById(interestId);
  }

  public Interest getById(Long interestId) {
    return interestRepository.getReferenceById(interestId);
  }

  public Interest save(@NotNull @Valid InterestInDTO interestDTO) {
    Interest interest = new Interest(interestDTO);
    interest.setCategories(categoryService.findMaxLimitByIdIn(interestDTO.categoryIds()));
    return interestRepository.save(interest);
  }

  public Interest update(@Valid Interest interest, @NotNull @Valid InterestInDTO interestDTO) {
    interest.setCategories(categoryService.findMaxLimitByIdIn(interestDTO.categoryIds()));
    interest.update(interestDTO);
    return interestRepository.save(interest);
  }

  public List<Interest> findAllSortByLikes() {
    return interestRepository.findAllSortByLikes();
  }

  public List<Interest> findAllLikedByAppUser(@Valid AppUser appUser) {
    return interestRepository.findAllByLikes_AppUser(appUser);
  }

  public List<LikedInterestsDTO> getLikedInterestsDTOS(@Valid AppUser appUser) {
    return interestRepository.findAllByLikes_AppUser(appUser).stream()
        .sorted(Comparator.comparing(i -> i.getName().toLowerCase()))
        .map(LikedInterestsDTO::new)
        .collect(Collectors.toList());
  }

  public List<InterestSuggestionDTO> getAllSuggestionDTOS() {
    return findAllSortByLikes().stream()
        .map(interest -> new InterestSuggestionDTO(interest, null))
        .collect(Collectors.toList());
  }

  public List<InterestSuggestionDTO> getAllSuggestionDTOS(@Valid CoordinatesDTO usersCoords) {
    return findAllSortByLikes().stream()
        .map(
            interest ->
                new InterestSuggestionDTO(
                    interest, getDistanceToNearestPlace(usersCoords, interest.getPlaces())))
        .sorted(Comparator.comparingDouble(InterestSuggestionDTO::distanceKm))
        .collect(Collectors.toList());
  }

  public List<RatedInterestDTO> getAllRatedInterestsDTOS(@Valid AppUser appUser) {
    List<Interest> ratedInterests =
        interestRepository.findAllDistinctByUserRatingsOrReviews(appUser);

    Comparator<PlaceReviewDTO> comparator =
        Comparator.comparing(
                (PlaceReviewDTO dto) -> Double.isNaN(dto.avgRating()) ? 0d : dto.avgRating())
            .reversed();

    return ratedInterests.stream()
        .map(
            interest ->
                new RatedInterestDTO(
                    interest, placeService.getPlaceReviewDTOs(appUser, interest, comparator)))
        .toList();
  }

  private Double getDistanceToNearestPlace(CoordinatesDTO usersCoords, List<Place> places) {
    if (places.isEmpty()) {
      return Double.NaN;
    }
    double minDistance = Double.MAX_VALUE;
    for (Place place : places) {
      double distance =
          haversine(
              usersCoords.latitude(),
              usersCoords.longitude(),
              place.getLatitude(),
              place.getLongitude());
      minDistance = Math.min(minDistance, distance);
    }
    return minDistance;
  }

  private double haversine(double lat1, double lon1, double lat2, double lon2) {
    // Haversine formula
    // https://www.geeksforgeeks.org/program-distance-two-points-earth/#:~:text=by%20Aayush%20Chaturvedi-,Java,-//%20Java%20program%20to
    lon1 = Math.toRadians(lon1);
    lat1 = Math.toRadians(lat1);
    lon2 = Math.toRadians(lon2);
    lat2 = Math.toRadians(lat2);

    // Haversine formula
    double dlon = lon2 - lon1;
    double dlat = lat2 - lat1;
    double a =
        Math.pow(Math.sin(dlat / 2), 2)
            + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
    double c = 2 * Math.asin(Math.sqrt(a));
    // Radius of earth in kilometers.
    final double r = 6371;
    return (c * r); // Distance in kilometers
  }
}
