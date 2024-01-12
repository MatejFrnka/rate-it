package it.rate.webapp.controllers.api;

import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.InterestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/interests")
public class InterestRestController {
  private final InterestService interestService;

  @GetMapping("/suggestions")
  public ResponseEntity<?> getAllSuggestions() {
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
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public ResponseEntity<?> getVotersByInterestId(@PathVariable Long interestId) {
    Interest interest = interestService.getById(interestId);
    return ResponseEntity.ok(interestService.getUsersDTO(interest, Role.RoleType.VOTER));
  }

  @GetMapping("/{interestId}/applications")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public ResponseEntity<?> getApplicantsByInterestId(@PathVariable Long interestId) {
    Interest interest = interestService.getById(interestId);
    return ResponseEntity.ok(interestService.getUsersDTO(interest, Role.RoleType.APPLICANT));
  }
}
