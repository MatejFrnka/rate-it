package it.rate.webapp.services;

import it.rate.webapp.config.security.ServerRole;
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

  public boolean hasRatingPermission(AppUser user, Interest interest) {
    Optional<Role> optRole = roleRepository.findById(new RoleId(user.getId(), interest.getId()));
    if (!interest.isExclusive()) {
      return true;
    } else if (optRole.isPresent()) {
      return optRole.get().getRole().equals(Role.RoleType.VOTER)
          || optRole.get().getRole().equals(Role.RoleType.CREATOR);
    }
    return false;
  }

  public boolean ratePlace(Long placeId) {
    Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    return canRateOrCreate(place.getInterest());
  }

  public boolean manageCommunity(Long interestId) {
    if (!interestRepository.existsById(interestId)) {
      throw new InterestNotFoundException();
    }
    AppUser user = userService.getAuthenticatedUser();
    if (user == null) {
      return false;
    }
    if (user.getServerRole().equals(ServerRole.ADMIN)) {
      return true;
    }
    Optional<Role> optRole = roleRepository.findById(new RoleId(user.getId(), interestId));
    return optRole.isPresent() && optRole.get().getRole().equals(Role.RoleType.CREATOR);
  }

  public boolean canCreateInterest() {
    return userService.getAuthenticatedUser() != null;
  }

  public boolean hasPlaceEditPermissions(Long placeId, Long interestId) {
    AppUser user = userService.getAuthenticatedUser();
    if (user == null) {
      return false;
    }
    // Check if user is admin
    if (user.getServerRole().equals(ServerRole.ADMIN)) {
      return true;
    }

    // Check if user is place creator
    Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    if (place.getCreator().equals(user)) {
      return true;
    }

    // Check if user is Interest creator
    Optional<Role> optRole = roleRepository.findById(new RoleId(user.getId(), interestId));
    return optRole.map(role -> role.getRole().equals(Role.RoleType.CREATOR)).orElse(false);
  }

  public boolean createPlace(Long interestId) {
    Interest interest =
        interestRepository.findById(interestId).orElseThrow(InterestNotFoundException::new);
    return canRateOrCreate(interest);
  }

  private boolean canRateOrCreate(Interest i) {
    AppUser user = userService.getAuthenticatedUser();
    if (user == null) {
      return false;
    }
    if (user.getServerRole().equals(ServerRole.ADMIN)) {
      return true;
    }
    Optional<Role> optRole = roleRepository.findById(new RoleId(user.getId(), i.getId()));
    if (!i.isExclusive()) {
      return true;
    }
    return optRole.isPresent()
        && (optRole.get().getRole().equals(Role.RoleType.VOTER)
            || optRole.get().getRole().equals(Role.RoleType.CREATOR));
  }
}
