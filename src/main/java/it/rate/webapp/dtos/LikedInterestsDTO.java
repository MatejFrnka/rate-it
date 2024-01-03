package it.rate.webapp.dtos;

import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;

import java.util.List;

public record LikedInterestsDTO(Long id, String name, String creator, Long followers) {

  public LikedInterestsDTO(Interest interest) {
    this(
        interest.getId(),
        interest.getName(),
        interest.getRoles().stream()
            .filter(role -> role.getRole().equals(Role.RoleType.CREATOR))
            .findFirst()
            .map(Role::getAppUser)
            .map(AppUser::getUsername)
            .orElse("Unknown creator"),
        (long) interest.countLikes());
  }
}
