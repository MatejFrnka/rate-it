package it.rate.webapp.services;

import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.InterestRepository;
import it.rate.webapp.repositories.PlaceRepository;
import it.rate.webapp.repositories.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PermissionService {

  private final PlaceRepository placeRepository;
  private final InterestRepository interestRepository;
  private final RoleRepository roleRepository;

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

  public String[] manageCommunity(Long interestId) {
    if (!interestRepository.existsById(interestId)) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Interest not found");
    }
    return new String[] {
      ServerRole.ADMIN.name(), String.format("ROLE_%s_%d", Role.RoleType.CREATOR.name(), interestId)
    };
  }

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
      return new String[] {ServerRole.USER.toString()};
    }
  }
}
