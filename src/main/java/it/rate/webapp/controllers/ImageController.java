package it.rate.webapp.controllers;

import it.rate.webapp.models.Place;
import it.rate.webapp.services.GoogleImageService;
import it.rate.webapp.services.PlaceService;
import java.io.IOException;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class ImageController {

  private final GoogleImageService googleImageService;
  private final PlaceService placeService;

  @PostMapping("interests/{interestId}/places/{placeId}/new-image")
  @PreAuthorize("@permissionService.ratePlace(#placeId)")
  public String uploadPlaceImage(
      @RequestParam("picture") MultipartFile file,
      @PathVariable Long interestId,
      @PathVariable Long placeId,
      Principal principal)
      throws IOException {

    String userEmail = principal.getName();
    Place place = placeService.getById(placeId);
    placeService.addImage(place, googleImageService.saveImage(file, userEmail));

    return "redirect:/interests/" + interestId + "/places/" + placeId;
  }
}
