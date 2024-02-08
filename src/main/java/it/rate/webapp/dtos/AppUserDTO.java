package it.rate.webapp.dtos;

import it.rate.webapp.models.AppUser;

public record AppUserDTO(
    Long id, String username, String bio, int followers, int follows, String imageName) {

  public AppUserDTO(AppUser appUser) {
    this(
        appUser.getId(),
        appUser.getUsername(),
        appUser.getBio(),
        appUser.getFollowers().size(),
        appUser.getFollows().size(),
        appUser.getImageName());
  }
}
