package it.rate.webapp.services;

import it.rate.webapp.config.security.ServerRole;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.models.Role;
import it.rate.webapp.repositories.PlaceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class PermissionService {
  private PlaceRepository placeRepository;

  public PermissionService(PlaceRepository placeRepository) {
    this.placeRepository = placeRepository;
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
      return new String[] {ServerRole.USER.toString()};
    }
  }
}
