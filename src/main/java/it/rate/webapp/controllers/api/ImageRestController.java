package it.rate.webapp.controllers.api;

import it.rate.webapp.exceptions.api.ApiServiceUnavailableException;
import it.rate.webapp.services.GoogleImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class ImageRestController {

  private final GoogleImageService googleImageService;

  @GetMapping("/{id}")
  public ResponseEntity<?> getImage(@PathVariable String id) {

    try {
      return ResponseEntity.ok().body(googleImageService.getImageById(id));
    } catch (ApiServiceUnavailableException e) {
      return ResponseEntity.internalServerError().build();
    }
  }

  @PostMapping("/new-interest-image")
  @PreAuthorize("@permissionService.canCreateInterest()")
  public ResponseEntity<?> uploadNewInterestImage(@RequestParam("picture") MultipartFile file) {

    try {
      return ResponseEntity.ok().body(googleImageService.saveImage(file));
    } catch (IOException e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
