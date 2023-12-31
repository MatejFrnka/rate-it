package it.rate.webapp.services;

import it.rate.webapp.dtos.CriteriaOfPlaceDTO;
import it.rate.webapp.dtos.CriterionAvgRatingDTO;
import it.rate.webapp.dtos.PlaceInfoDTO;
import it.rate.webapp.exceptions.BadRequestException;
import it.rate.webapp.models.*;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RatingRepository;

import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlaceService {

  private PlaceRepository placeRepository;
  private UserService userService;
  private InterestService interestService;
  private RatingRepository ratingRepository;

  public Place savePlace(Place place, Long interestId) throws BadRequestException {
    String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();

    AppUser loggedUser =
        userService
            .findByEmail(loggedInUserName)
            .orElseThrow(() -> new BadRequestException("User email not found in the database"));
    Interest interest =
        interestService
            .findInterestById(interestId)
            .orElseThrow(() -> new BadRequestException("Interest ID not found in the database"));

    loggedUser.getCreatedPlaces().add(place);
    interest.getPlaces().add(place);
    place.setCreator(loggedUser);
    place.setInterest(interest);

    return placeRepository.save(place);
  }

  public Optional<Place> findById(Long id) {
    return placeRepository.findById(id);
  }

  public boolean isCreator(String loggedUserEmail, Long placeId) throws BadRequestException {

    AppUser appUser =
        userService
            .findByEmail(loggedUserEmail)
            .orElseThrow(() -> new BadRequestException("Email not found in database"));

    Place place =
        placeRepository
            .findById(placeId)
            .orElseThrow(() -> new BadRequestException("Place id not found in database"));

    return place.getCreator().equals(appUser);
  }

  public Place getReferenceById(Long placeId) {
    return placeRepository.getReferenceById(placeId);
  }

  public CriteriaOfPlaceDTO getCriteriaOfPlaceDTO(Place place) {
    List<CriterionAvgRatingDTO> criteriaAvgRatingDTOs = new ArrayList<>();

    place
        .getInterest()
        .getCriteria()
        .forEach(
            criterion -> criteriaAvgRatingDTOs.add(getCriterionAvgRatingDTO(criterion, place)));

    return new CriteriaOfPlaceDTO(criteriaAvgRatingDTOs);
  }

  public List<PlaceInfoDTO> getPlaceInfoDTOS(Interest interest) {
    return interest.getPlaces().stream().map(this::getPlaceInfoDTO).collect(Collectors.toList());
  }

  private PlaceInfoDTO getPlaceInfoDTO(Place place) {
    Set<CriterionAvgRatingDTO> criteria =
        place.getInterest().getCriteria().stream()
            .map(criterion -> getCriterionAvgRatingDTO(criterion, place))
            .collect(Collectors.toSet());
    CriterionAvgRatingDTO bestCriterion = getBestRatedCriterion(criteria);
    CriterionAvgRatingDTO worstCriterion = getWorstRatedCriterion(criteria);
    return new PlaceInfoDTO(place, bestCriterion, worstCriterion);
  }

  private CriterionAvgRatingDTO getCriterionAvgRatingDTO(Criterion criterion, Place place) {
    double avgRating =
        ratingRepository.findAllByCriterionAndPlace(criterion, place).stream()
            .mapToDouble(Rating::getScore)
            .average()
            .orElse(-1);

    return new CriterionAvgRatingDTO(criterion, avgRating);
  }

  private CriterionAvgRatingDTO getBestRatedCriterion(
      Set<CriterionAvgRatingDTO> criteriaAvgRatingDTOs) {
    if (!criteriaAvgRatingDTOs.isEmpty()) {
      return criteriaAvgRatingDTOs.stream()
          .max(Comparator.comparingDouble(CriterionAvgRatingDTO::avgRating))
          .get();
    }
    throw new IllegalStateException("No criteria found");
  }

  private CriterionAvgRatingDTO getWorstRatedCriterion(
      Set<CriterionAvgRatingDTO> criteriaAvgRatingDTOs) {
    if (!criteriaAvgRatingDTOs.isEmpty()) {
      return criteriaAvgRatingDTOs.stream()
          .min(Comparator.comparingDouble(CriterionAvgRatingDTO::avgRating))
          .get();
    }
    throw new IllegalStateException("No criteria found");
  }
}
