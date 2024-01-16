package it.rate.webapp.services;

import it.rate.webapp.dtos.CoordinatesDTO;
import it.rate.webapp.dtos.InterestSuggestionDTO;
import it.rate.webapp.dtos.InterestUserDTO;
import it.rate.webapp.dtos.LikedInterestsDTO;
import it.rate.webapp.exceptions.badrequest.InvalidInterestDetailsException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.*;
import java.util.*;
import java.util.stream.Collectors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class InterestService {

  private InterestRepository interestRepository;
  private RoleRepository roleRepository;
  private CriterionRepository criterionRepository;
  private UserRepository userRepository;

  public Optional<Interest> findById(Long id) {
    return interestRepository.findById(id);
  }

  public Interest getById(Long id) {
    return interestRepository.getReferenceById(id);
  }

  public void setApplicantRole(Long interestId) {
    Interest interest =
        interestRepository.findById(interestId).orElseThrow(InvalidInterestDetailsException::new);
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    AppUser currentUser = userRepository.getByEmail(authentication.getName());

    roleRepository.save(new Role(currentUser, interest, Role.RoleType.APPLICANT));
  }

  public List<Interest> findAllSortByLikes() {
    return interestRepository.findAllSortByLikes();
  }

  public List<InterestSuggestionDTO> getAllSuggestionDtos() {
    return findAllSortByLikes().stream()
        .map(interest -> new InterestSuggestionDTO(interest, null))
        .collect(Collectors.toList());
  }

  public List<InterestSuggestionDTO> getAllSuggestionDtos(CoordinatesDTO usersCoords) {
    return findAllSortByLikes().stream()
        .map(
            interest ->
                new InterestSuggestionDTO(
                    interest, getDistanceToNearestPlace(usersCoords, interest.getPlaces())))
        .sorted(Comparator.comparingDouble(InterestSuggestionDTO::distanceKm))
        .collect(Collectors.toList());
  }

  public List<Interest> getLikedInterests(String loggedUser) {
    return interestRepository.findAllByLikes_AppUser_Email(loggedUser);
  }

  public Interest saveEditedInterest(@Valid Interest interest, @NotEmpty List<@NotBlank String> criteriaNames) {
    List<String> oldCriteriaNames =
        interestRepository.getReferenceById(interest.getId()).getCriteria().stream()
            .map(Criterion::getName)
            .toList();

    List<Criterion> newCriteria =
        criteriaNames.stream()
            .filter(name -> !oldCriteriaNames.contains(name))
            .map(name -> Criterion.builder().name(name).build())
            .toList();

    for (String name : oldCriteriaNames) {
      if (!criteriaNames.contains(name)) {
        criterionRepository.deleteByNameAndInterestId(name, interest.getId());
      }
    }

    criterionRepository.saveAll(newCriteria);
    newCriteria.forEach(c -> c.setInterest(interest));

    return interestRepository.save(interest);
  }

  public List<LikedInterestsDTO> getLikedInterestsDTOS(@NotBlank String loggedUser) {
    return interestRepository.findAllByLikes_AppUser_Email(loggedUser).stream()
        .sorted(Comparator.comparing(i -> i.getName().toLowerCase()))
        .map(LikedInterestsDTO::new)
        .collect(Collectors.toList());
  }

  public Interest save(Interest interest) {
    return interestRepository.save(interest);
  }

  public List<InterestUserDTO> getUsersDTO(Interest interest, @NotNull Role.RoleType role) {

    return interest.getRoles().stream()
        .filter(r -> r.getRole().equals(role))
        .map(InterestUserDTO::new)
        .sorted(Comparator.comparing(dto -> dto.userName().toLowerCase()))
        .collect(Collectors.toList());
  }

  private double getDistanceToNearestPlace(CoordinatesDTO usersCoords, List<Place> places) {
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
