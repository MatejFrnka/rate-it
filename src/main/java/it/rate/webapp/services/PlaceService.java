package it.rate.webapp.services;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.repositories.PlaceRepository;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlaceService {

  private PlaceRepository placeRepository;
  private UserService userService;
  private InterestService interestService;

  public Place saveNewPlace(Place place, Long interestId) {

    String loggedInUserName = SecurityContextHolder.getContext().getAuthentication().getName();

    AppUser loggedUser = userService.findByEmail(loggedInUserName)
            .orElseThrow(() -> new RuntimeException("User email not found in the database"));
    Interest interest = interestService.findInterestById(interestId).
            orElseThrow(() -> new RuntimeException("Interest ID not found in the database"));

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

  }
}
