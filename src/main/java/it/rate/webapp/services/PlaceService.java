package it.rate.webapp.services;

import it.rate.webapp.dtos.CriteriaOfPlaceDTO;
import it.rate.webapp.dtos.CriterionAvgRatingDTO;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.PlaceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.rate.webapp.repositories.RatingRepository;
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

  public Place savePlace(Place place, Long interestId) {

    String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();

    AppUser loggedUser =
        userService
            .findByEmail(loggedInUserName)
            .orElseThrow(() -> new RuntimeException("User email not found in the database"));
    Interest interest =
        interestService
            .findInterestById(interestId)
            .orElseThrow(() -> new RuntimeException("Interest ID not found in the database"));

    loggedUser.getCreatedPlaces().add(place);
    interest.getPlaces().add(place);
    place.setCreator(loggedUser);
    place.setInterest(interest);

    return placeRepository.save(place);
  }

  public Optional<Place> findById(Long id) {
    return placeRepository.findById(id);
  }

  public boolean isCreator(String loggedUserEmail, Long placeId) {

    AppUser appUser =
        userService
            .findByEmail(loggedUserEmail)
            .orElseThrow(() -> new RuntimeException("Email not found in database"));

    Place place =
        placeRepository
            .findById(placeId)
            .orElseThrow(() -> new RuntimeException("Place id not found in database"));

    return place.getCreator().equals(appUser);
  }

  public Place getReferenceById(Long placeId) {
    return placeRepository.getReferenceById(placeId);
  }

  public CriteriaOfPlaceDTO getCriteriaOfPlaceDTO(Place place) {
    List<Criterion> criteria = place.getInterest().getCriteria();
    List<CriterionAvgRatingDTO> criteriaAvgRatingDTOs = new ArrayList<>();
    criteria.forEach(criterion -> {
      double avgRating = ratingRepository.findAllByCriterionAndPlace(criterion, place)
              .stream()
              .mapToDouble(Rating::getScore)
              .average()
              .orElse(-1);
      criteriaAvgRatingDTOs.add(new CriterionAvgRatingDTO(criterion, avgRating));
    });
    return new CriteriaOfPlaceDTO(criteriaAvgRatingDTOs);
  }
}
