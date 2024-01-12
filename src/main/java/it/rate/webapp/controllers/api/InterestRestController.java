package it.rate.webapp.controllers.api;

import it.rate.webapp.dtos.CoordinatesDTO;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.InterestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/interests")
public class InterestRestController {
  private final InterestService interestService;

  @GetMapping("/suggestions")
  public ResponseEntity<?> getAllSuggestions(
      @Validated @RequestBody Optional<CoordinatesDTO> usersCoords) {
    if (usersCoords.isPresent()) {
      return ResponseEntity.ok().body(interestService.getAllSuggestionDtos(usersCoords.get()));
    }
    return ResponseEntity.ok().body(interestService.getAllSuggestionDtos());
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @GetMapping("/my")
  public ResponseEntity<?> getMyInterestsSuggestions(Principal principal) {
    if (principal == null) {
      return ResponseEntity.badRequest().body("User not found");
    }
    return ResponseEntity.ok().body(interestService.getLikedInterestsDTOS(principal.getName()));
  }

  @GetMapping("/{interestId}/users")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public ResponseEntity<?> getVotersByInterestId(@PathVariable Long interestId) {
    Interest interest = interestService.getById(interestId);
    return ResponseEntity.ok(interestService.getUsersDTO(interest, Role.RoleType.VOTER));
  }

  @GetMapping("/{interestId}/applications")
  @PreAuthorize("hasAnyAuthority(@permissionService.manageCommunity(#interestId))")
  public ResponseEntity<?> getApplicantsByInterestId(@PathVariable Long interestId) {
    Interest interest = interestService.getById(interestId);
    return ResponseEntity.ok(interestService.getUsersDTO(interest, Role.RoleType.APPLICANT));
  }
}
