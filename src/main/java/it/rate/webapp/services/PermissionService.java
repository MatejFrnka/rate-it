package it.rate.webapp.services;

import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.config.security.ValidatePermissions;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.models.Role;
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

  @ValidatePermissions
  public boolean hasRatingPermission(AppUser user, Interest interest) {
    Optional<Role> optRole =
        roleRepository.findByAppUserIdAndInterestId(user.getId(), interest.getId());
    if (!interest.isExclusive()) {
      return true;
    } else if (optRole.isPresent()) {
      return optRole.get().getRole().equals(Role.RoleType.VOTER)
          || optRole.get().getRole().equals(Role.RoleType.CREATOR);
    }
    return false;
  }

  @ValidatePermissions
  public String[] ratePlace(Long placeId) {
    Optional<Place> optPlace = placeRepository.findById(placeId);
    if (optPlace.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
    }
    Interest i = optPlace.get().getInterest();

    if (i.isExclusive()) {
      return new String[] {
        String.format("ROLE_%s_%d", Role.RoleType.VOTER.name(), i.getId()),
        String.format("ROLE_%s_%d", Role.RoleType.CREATOR.name(), i.getId())
      };
    } else {
      return new String[] {ServerRole.USER.toString(), ServerRole.ADMIN.toString()};
    }
  }

  @ValidatePermissions
  public String[] manageCommunity(Long interestId) {
    if (!interestRepository.existsById(interestId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    return new String[] {
      ServerRole.ADMIN.name(), String.format("ROLE_%s_%d", Role.RoleType.CREATOR.name(), interestId)
    };
  }

  @ValidatePermissions
  public boolean hasPlaceEditPermissions(Long placeId, Long interestId) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication.getPrincipal().equals("anonymousUser")) {
      return false;
    }
    Optional<AppUser> optUser = userRepository.findByEmail(authentication.getName());
    if (optUser.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
    AppUser user = optUser.get();
    // Check if user is admin
    if (user.getServerRole().equals(ServerRole.ADMIN)) {
      return true;
    }
    // Check if user is Interest creator
    Optional<Role> optRole =
        user.getRoles().stream()
            .filter(r -> r.getRole().equals(Role.RoleType.CREATOR))
            .filter(r -> r.getInterest().getId().equals(interestId))
            .findFirst();
    if (optRole.isPresent()) {
      return true;
    }
    Optional<Place> optPlace = placeRepository.findById(placeId);
    if (optPlace.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
    }
    Place place = optPlace.get();
    // Check if User is Place creator
    return place.getCreator().getId().equals(user.getId());
  }

  @ValidatePermissions
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
}
