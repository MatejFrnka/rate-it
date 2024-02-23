package it.rate.webapp.controllers.api;

import it.rate.webapp.dtos.CoordinatesDTO;
import it.rate.webapp.dtos.ErrorMessagesDTO;
import it.rate.webapp.dtos.InterestSuggestionDTO;
import it.rate.webapp.dtos.LikeDTO;
import it.rate.webapp.exceptions.notfound.InterestNotFoundException;
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
  public ResponseEntity<?> getAllSuggestions(
      Optional<Double> latitude, Optional<Double> longitude) {
    if (latitude.isPresent() || longitude.isPresent()) {
      CoordinatesDTO usersCoords =
          new CoordinatesDTO(latitude.orElse(null), longitude.orElse(null));
      return handleCoordinates(usersCoords);
    }
    return ResponseEntity.ok().body(interestService.getAllSuggestionDTOS());
  }

  @GetMapping("/my")
  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  public ResponseEntity<?> getMyInterestsSuggestions(Principal principal) {
    AppUser loggedUser = userService.getByEmail(principal.getName());
    return ResponseEntity.ok().body(interestService.getLikedInterestsDTOS(loggedUser));
  }

  @GetMapping("/{interestId}/users")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public ResponseEntity<?> getVotersByInterestId(@PathVariable Long interestId) {
    Interest interest = interestService.getById(interestId);
    return ResponseEntity.ok(userService.getUsersDTO(interest, Role.RoleType.VOTER));
  }

  @GetMapping("/{interestId}/applications")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public ResponseEntity<?> getApplicantsByInterestId(@PathVariable Long interestId) {
    Interest interest = interestService.getById(interestId);
    return ResponseEntity.ok(userService.getUsersDTO(interest, Role.RoleType.APPLICANT));
  }

  @GetMapping("/{interestId}/places")
  public ResponseEntity<?> getAllPlaceInfoDTOs(@PathVariable Long interestId) {
    Optional<Interest> optInterest = interestService.findById(interestId);
    if (optInterest.isPresent()) {
      return ResponseEntity.ok().body(placeService.getPlaceInfoDTOS(optInterest.get()));
    }
    return ResponseEntity.badRequest().body("This interest doesn't exist");
  }

  @PostMapping("/{interestId}/like")
  @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
  public ResponseEntity<?> like(
      @PathVariable Long interestId, @RequestBody LikeDTO like, Principal principal) {

    AppUser loggedUser = userService.getByEmail(principal.getName());
    Interest interest = interestService.getById(interestId);
    likeService.setLike(loggedUser, interest, like.liked());

    return ResponseEntity.ok().body(like);
  }

  private ResponseEntity<?> handleCoordinates(CoordinatesDTO coordinates) {
    Set<ConstraintViolation<CoordinatesDTO>> validationErrors = validator.validate(coordinates);
    if (validationErrors.isEmpty()) {
      return ResponseEntity.ok().body(interestService.getAllSuggestionDTOS(coordinates));
    }
    return ResponseEntity.badRequest()
        .body(
            new ErrorMessagesDTO(
                validationErrors.stream().map(ConstraintViolation::getMessage).toList()));
  }

  @GetMapping("/{interestId}")
  public ResponseEntity<?> getSuggestion(@PathVariable Long interestId) {
    Optional<Interest> optInterest = interestService.findById(interestId);
    if (optInterest.isEmpty()) {
      return ResponseEntity.badRequest().body(new InterestNotFoundException());
    }
    return ResponseEntity.ok().body(new InterestSuggestionDTO(optInterest.get(), null));
  }
}
