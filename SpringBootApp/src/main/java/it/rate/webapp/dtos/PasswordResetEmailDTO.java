package it.rate.webapp.dtos;

import it.rate.webapp.models.AppUser;

public record PasswordResetEmailDTO(
    String email, String username, String subject, String token, Long userId) {
  public PasswordResetEmailDTO(AppUser user, String token) {
    this(
        user.getEmail(),
        user.getUsername(),
        "Reset your RateSpot account password",
        token,
        user.getId());
  }
}
