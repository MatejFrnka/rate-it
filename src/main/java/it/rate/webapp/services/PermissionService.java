package it.rate.webapp.services;

import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.config.security.UpdateSecurityContext;
import it.rate.webapp.models.*;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RoleRepository;
import it.rate.webapp.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PermissionService {

  private final PlaceRepository placeRepository;
  private final InterestRepository interestRepository;
  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

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
    Optional<Place> optPlace = placeRepository.findById(placeId);
    if (optPlace.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
    }
    Interest i = optPlace.get().getInterest();
    AppUser user = authenticatedUser();
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

  public boolean manageCommunity(Long interestId) {
    if (!interestRepository.existsById(interestId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    AppUser user = authenticatedUser();
    if (user == null) {
      return false;
    }
    Optional<Role> optRole = roleRepository.findById(new RoleId(user.getId(), interestId));
    if (optRole.isEmpty()) {
      return false;
    }
    return optRole.get().getRole().equals(Role.RoleType.CREATOR)
        || user.getServerRole().equals(ServerRole.ADMIN);
  }

  public boolean hasPlaceEditPermissions(Long placeId, Long interestId) {
    AppUser user = authenticatedUser();
    if (user == null) {
      return false;
    }
    // Check if user is admin
    if (user.getServerRole().equals(ServerRole.ADMIN)) {
      return true;
    }

    // Check if user is place creator
    Optional<Place> optPlace = placeRepository.findById(placeId);
    if (optPlace.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
    }
    if (optPlace.get().getCreator().equals(user)) {
      return true;
    }

    // Check if user is Interest creator
    Optional<Role> optRole = roleRepository.findById(new RoleId(user.getId(), interestId));
    return optRole.map(role -> role.getRole().equals(Role.RoleType.CREATOR)).orElse(false);
  }

  @UpdateSecurityContext
  public String[] createPlace(Long interestId) {
    Optional<Interest> optInterest = interestRepository.findById(interestId);
    if (optInterest.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
    }
    Interest i = optInterest.get();

    if (i.isExclusive()) {
      return new String[] {
        String.format("ROLE_%s_%d", Role.RoleType.VOTER.name(), i.getId()),
        String.format("ROLE_%s_%d", Role.RoleType.CREATOR.name(), i.getId()),
        ServerRole.ADMIN.name()
      };
    } else {
      return new String[] {ServerRole.USER.toString(), ServerRole.ADMIN.toString()};
    }
  }

  private AppUser authenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && !(authentication.getPrincipal().equals("anonymousUser"))) {
      return userRepository.getByEmail(authentication.getName());
    }
    return null;
  }
}
