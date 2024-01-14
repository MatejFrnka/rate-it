package it.rate.webapp.controllers;

import it.rate.webapp.services.GoogleImageService;
import it.rate.webapp.services.PlaceService;
import java.io.IOException;
import java.nio.file.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
public class ImageController {

  private final GoogleImageService googleImageService;
  private final PlaceService placeService;

  @PostMapping("interests/{interestId}/places/{placeId}")
  @PreAuthorize("@permissionService.ratePlace(#placeId)")
  public String uploadPlaceImage(
      @RequestParam("picture") MultipartFile file,
      @PathVariable Long interestId,
      @PathVariable Long placeId) {

    try {
      placeService.addImage(placeId, googleImageService.savePlaceImage(file, placeId));
    } catch (IOException e) {
      throw new ResponseStatusException(
          HttpStatus.INTERNAL_SERVER_ERROR, "Upload was not successful");
    }

    return "redirect:/interests/" + interestId + "/places/" + placeId;
  }
}
