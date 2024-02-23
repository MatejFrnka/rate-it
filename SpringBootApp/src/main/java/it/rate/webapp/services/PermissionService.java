package it.rate.webapp.services;

import it.rate.webapp.config.ServerRole;
import it.rate.webapp.exceptions.notfound.InterestNotFoundException;
import it.rate.webapp.exceptions.notfound.PlaceNotFoundException;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PermissionService {

  private final PlaceRepository placeRepository;
  private final InterestRepository interestRepository;
  private final RoleRepository roleRepository;
  private final UserService userService;
  private final RatingService ratingService;
  private final ReviewService reviewService;

  public boolean hasRatingPermission(AppUser user, Interest interest) {
    if (!interest.isExclusive()) {
      return true;
    }
    return isVoterOrCreator(user, interest.getId());
  }

  public boolean ratePlace(Long placeId) {
    Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    return canRateOrCreate(place.getInterest()) || hasRatedOrReviewedPlace(place);
  }

  public boolean manageCommunity(Long interestId) {
    if (!interestRepository.existsById(interestId)) {
      throw new InterestNotFoundException();
    }
    AppUser user = userService.getAuthenticatedUser();
    if (isAuthenticated(user)) {
      return isAdmin(user) || isCreator(user, interestId);
    }
    return false;
  }

  public boolean canCreateInterest() {
    return userService.getAuthenticatedUser() != null;
  }

  public boolean hasPlaceEditPermissions(Long placeId, Long interestId) {
    AppUser user = userService.getAuthenticatedUser();
    Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);

    if (isAuthenticated(user)) {
      return (isAdmin(user) || place.getCreator().equals(user) || isCreator(user, interestId));
    }
    return false;
  }

  public boolean createPlace(Long interestId) {
    Interest interest =
        interestRepository.findById(interestId).orElseThrow(InterestNotFoundException::new);
    return canRateOrCreate(interest);
  }

  private boolean canRateOrCreate(Interest i) {
    AppUser user = userService.getAuthenticatedUser();
    if (isAuthenticated(user)) {
      return (isAdmin(user) || !i.isExclusive() || isVoterOrCreator(user, i.getId()));
    }
    return false;
  }

  public boolean hasRatedOrReviewedPlace(Place place) {
    AppUser user = userService.getAuthenticatedUser();
    return isAuthenticated(user)
        && (ratingService.existsByUserAndPlace(user, place)
            || reviewService.findById(new ReviewId(user.getId(), place.getId())).isPresent());
  }

  private boolean isAuthenticated(AppUser user) {
    return user != null;
  }

  private boolean isAdmin(AppUser user) {
    return user.getServerRole().equals(ServerRole.ADMIN);
  }

  private boolean isCreator(AppUser user, Long interestId) {
    Optional<Role> optRole = roleRepository.findById(new RoleId(user.getId(), interestId));
    return optRole.map(role -> role.getRoleType().equals(Role.RoleType.CREATOR)).orElse(false);
  }

  private boolean isVoterOrCreator(AppUser user, Long interestId) {
    Optional<Role> optRole = roleRepository.findById(new RoleId(user.getId(), interestId));
    return optRole
        .map(
            role ->
                role.getRoleType().equals(Role.RoleType.VOTER)
                    || role.getRoleType().equals(Role.RoleType.CREATOR))
        .orElse(false);
  }
  
  public boolean canEditUser(String username) {
    if (username == null || username.isBlank()) {
      return false;
    }
    return username.equalsIgnoreCase(userService.getAuthenticatedUser().getUsername());
  }

  public boolean canEditUser(Long userId) {
    if (userId == null) {
      return false;
    }
    return userId.equals(userService.getAuthenticatedUser().getId());
  }
}
