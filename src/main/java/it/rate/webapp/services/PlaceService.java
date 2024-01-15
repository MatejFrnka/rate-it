package it.rate.webapp.services;

import it.rate.webapp.dtos.CriteriaOfPlaceDTO;
import it.rate.webapp.dtos.CriterionAvgRatingDTO;
import it.rate.webapp.dtos.PlaceInfoDTO;
import it.rate.webapp.exceptions.badrequest.BadRequestException;
import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.exceptions.badrequest.InvalidUserDetailsException;
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
        userService.findByEmail(loggedInUserName).orElseThrow(InvalidUserDetailsException::new);
    Interest interest =
        interestService
            .findById(interestId)
            .orElseThrow(InvalidInterestDetailsException::new);

    loggedUser.getCreatedPlaces().add(place);
    interest.getPlaces().add(place);
    place.setCreator(loggedUser);
    place.setInterest(interest);

    return placeRepository.save(place);
  }

  public Optional<Place> findById(Long id) {
    return placeRepository.findById(id);
  }

  public Place getById(Long placeId) {
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
            .mapToDouble(Rating::getRating)
            .average()
            .orElse(-1);

    return new CriterionAvgRatingDTO(criterion.getId(), criterion.getName(), avgRating);
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

  public void addImage(Long placeId, String imageId) {
    Place place = placeRepository.getReferenceById(placeId);
    place.getImageNames().add(imageId);
    placeRepository.save(place);
  }
}
