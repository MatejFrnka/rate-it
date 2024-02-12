package it.rate.webapp.controllers.api;

import it.rate.webapp.dtos.ImageUploadResponseDTO;
import it.rate.webapp.exceptions.api.ApiServiceUnavailableException;
import it.rate.webapp.models.AppUser;
import it.rate.webapp.models.Interest;
import it.rate.webapp.models.Place;
import it.rate.webapp.services.*;
import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageRestController {

  private final ImageService imageService;
  private final InterestService interestService;
  private final PlaceService placeService;
  private final UserService userService;

  @GetMapping("/{id}")
  public ResponseEntity<?> getImage(@PathVariable String id) {

    try {
      return ResponseEntity.ok().body(imageService.getImageById(id));
    } catch (ApiServiceUnavailableException e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping("/new-interest-image")
  @PreAuthorize("@permissionService.canCreateInterest()")
  public ResponseEntity<?> uploadNewInterestImage(
      @RequestParam("picture") MultipartFile file, Principal principal) {

    String userEmail = principal.getName();
    try {
      return ResponseEntity.ok()
          .body(new ImageUploadResponseDTO(imageService.saveImage(file, userEmail)));
    } catch (IOException e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @PutMapping("/interests/{interestId}/edit")
  @PreAuthorize("@permissionService.manageCommunity(#interestId)")
  public ResponseEntity<?> changeInterestImage(
      @RequestParam("picture") MultipartFile file,
      @PathVariable Long interestId,
      Principal principal) {

    Interest interest = interestService.getById(interestId);
    String userEmail = principal.getName();

    try {
      return ResponseEntity.ok()
          .body(
              new ImageUploadResponseDTO(
                  imageService.changeInterestImage(interest, file, userEmail)));
    } catch (ApiServiceUnavailableException e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping("/{placeId}/new-place-image")
  @PreAuthorize("@permissionService.ratePlace(#placeId)")
  public ResponseEntity<?> uploadPlaceImage(
      @RequestParam("picture") MultipartFile file,
      @PathVariable Long placeId,
      Principal principal) {

    String userEmail = principal.getName();
    Place place = placeService.getById(placeId);
    try {
      placeService.addImage(place, imageService.saveImage(file, userEmail));
      return ResponseEntity.ok().build();
    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("Error while processing the file");
    }
  }

  @PostMapping("/users/{username}/new-profile-image")
  @PreAuthorize("@permissionService.canEditUser(#username)")
  public ResponseEntity<?> uploadProfileImage(
      @PathVariable String username,
      @RequestParam("picture") MultipartFile file,
      Principal principal) {

    String userEmail = principal.getName();
    Optional<AppUser> user = userService.findByUsernameIgnoreCase(username);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest().body("User does not exist");
    }

    try {
      userService.addImage(imageService.saveImage(file, userEmail), user.get());
      return ResponseEntity.ok().build();

    } catch (IOException e) {
      return ResponseEntity.internalServerError().body("Error while processing the file");
    }
  }

  @GetMapping("/users/{username}")
  public ResponseEntity<?> getProfilePicture(@PathVariable String username) {

    Optional<AppUser> user = userService.findByUsernameIgnoreCase(username);

    if (user.isEmpty()) {
      return ResponseEntity.badRequest().body("User does not exist");
    } else if (user.get().getImageName() == null) {
      return ResponseEntity.noContent().build();
    }

    try {
      return ResponseEntity.ok().body(imageService.getImageById(user.get().getImageName()));
    } catch (ApiServiceUnavailableException e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
