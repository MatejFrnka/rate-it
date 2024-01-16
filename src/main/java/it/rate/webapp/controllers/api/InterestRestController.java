package it.rate.webapp.controllers.api;

import it.rate.webapp.dtos.CoordinatesDTO;
import it.rate.webapp.dtos.ErrorMessagesDTO;
import it.rate.webapp.dtos.LikeDTO;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Role;
import it.rate.webapp.services.InterestService;
import it.rate.webapp.services.LikeService;
import it.rate.webapp.services.PlaceService;
import it.rate.webapp.services.UserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/interests")
public class InterestRestController {
  private final Validator validator;
  private final InterestService interestService;
  private final PlaceService placeService;
  private final UserService userService;
  private final LikeService likeService;

  @GetMapping("/suggestions")
  public ResponseEntity<?> getAllSuggestions(@RequestBody Optional<CoordinatesDTO> usersCoords) {
    if (usersCoords.isPresent()) {
      return handleCoordinates(usersCoords.get());
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

  @GetMapping("/{interestId}/places")
  public ResponseEntity<?> getAllPlaceInfoDTOs(@PathVariable Long interestId) {
    Optional<Interest> optInterest = interestService.findById(interestId);
    if (optInterest.isPresent()) {
      return ResponseEntity.ok().body(placeService.getPlaceInfoDTOS(optInterest.get()));
    }
    return ResponseEntity.badRequest().body("This interest doesn't exist");
  }

  private ResponseEntity<?> handleCoordinates(CoordinatesDTO coordinates) {
    Set<ConstraintViolation<CoordinatesDTO>> validationErrors = validator.validate(coordinates);
    if (validationErrors.isEmpty()) {
      return ResponseEntity.ok().body(interestService.getAllSuggestionDtos(coordinates));
    }
    return ResponseEntity.badRequest()
        .body(
            new ErrorMessagesDTO(
                validationErrors.stream().map(ConstraintViolation::getMessage).toList()));
  }

  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  @PostMapping("/{interestId}/like")
  public ResponseEntity<?> like(@PathVariable Long interestId, @RequestBody LikeDTO like, Principal principal) {
    if (principal != null) {
      AppUser loggedUser = userService.getByEmail(principal.getName());
      Interest interest = interestService.getById(interestId);
      likeService.changeLike(loggedUser, interest, like.liked());
    }
    return ResponseEntity.ok().build();
  }
}
